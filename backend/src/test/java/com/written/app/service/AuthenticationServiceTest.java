package com.written.app.service;

import com.written.app.dto.AuthenticationResponse;
import com.written.app.dto.RegisterRequest;
import com.written.app.model.Role;
import com.written.app.model.User;
import com.written.app.repository.TokenRepository;
import com.written.app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    public void AuthenticationService_Register_ReturnAuthResponse() {
        // given
        RegisterRequest request = new RegisterRequest("test@example.com", "password");

        User savedUser = User.builder()
                .id(1)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nick("") // default
                .isDeleted(false) // default
                .createdAt(LocalDateTime.now()) // default
                .role(Role.USER)
                .build();
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        // when
        AuthenticationResponse response = authenticationService.register(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("jwtToken");
        assertThat(response.getRefreshToken()).isEqualTo("refreshToken");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getEmail()).isEqualTo(request.getEmail());
        assertThat(capturedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(capturedUser.getRole()).isEqualTo(Role.USER);

        verify(jwtService).generateToken(savedUser);
        verify(jwtService).generateRefreshToken(savedUser);
        verify(tokenRepository).save(any());
    }
}
