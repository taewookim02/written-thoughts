package com.written.app.service;

import com.written.app.dto.ChangePasswordRequestDto;
import com.written.app.model.Role;
import com.written.app.model.User;
import com.written.app.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1)
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    public void UserService_Delete_ReturnVoid() {
        // given
        UsernamePasswordAuthenticationToken authToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authToken.getPrincipal()).thenReturn(user);

        // when
        userService.delete(authToken);

        // then
        verify(userRepository).delete(user);
    }

    @Test
    public void UserService_ChangePassword_ReturnVoid() {
        // given
        UsernamePasswordAuthenticationToken authToken = mock(UsernamePasswordAuthenticationToken.class);
        when(authToken.getPrincipal()).thenReturn(user);

        ChangePasswordRequestDto inputDto = ChangePasswordRequestDto.builder()
                .currentPassword("currentPassword")
                .newPassword("newPassword")
                .confirmationPassword("newPassword")
                .build();
        when(passwordEncoder.matches("currentPassword", user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        // when
        userService.changePassword(inputDto, authToken);

        // then
        assertThat(user.getPassword()).isEqualTo("encodedNewPassword");

        verify(userRepository).save(any(User.class));
    }
}
