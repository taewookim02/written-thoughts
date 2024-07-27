package com.written.app.service;

import com.written.app.model.Token;
import com.written.app.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogoutServiceTest {
    @Mock
    private TokenRepository tokenRepository;
    @InjectMocks
    private LogoutService logoutService;

    @Test
    public void LogoutService_Logout_ReturnVoid() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);

        String jwt = "validJwtToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);

        Token storedToken = new Token();
        when(tokenRepository.findByToken(jwt)).thenReturn(Optional.of(storedToken));

        // when
        logoutService.logout(request, response, authentication);

        // then
        assertThat(storedToken.isExpired()).isTrue();
        assertThat(storedToken.isRevoked()).isTrue();
        verify(tokenRepository).save(storedToken);

    }
}
