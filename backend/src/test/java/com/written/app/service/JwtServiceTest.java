package com.written.app.service;

import com.written.app.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private String secretKey = "secretKeySecretKeySecretKeySecretKeySecretKeySecretKeySecretKeySecretKeySecretKey";

    private UserDetails userDetails;
    private String username;

    @BeforeEach
    public void setUp() {
        // @Value in jwt
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", 604800000L);

        username = "test@example.com";
        userDetails = User.builder()
                .email(username)
                .password("password")
                .tokens(new ArrayList<>())
                .build();
    }


    @Test
    public void JwtService_ExtractUsername_ReturnUsername() {
        // given
        String token = jwtService.generateToken(userDetails);


        // when
        String extractedUsername = jwtService.extractUsername(token);

        // then
        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    public void JwtService_GenerateToken_ReturnToken() {
        // given

        // when
        String token = jwtService.generateToken(userDetails);

        // then
        assertThat(token).isNotBlank();
        assertThat(jwtService.extractUsername(token)).isEqualTo(username);
    }

    @Test
    public void JwtService_IsTokenValid_ReturnTrue() {
        // given
        String token = jwtService.generateToken(userDetails);

        // when
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    public void JwtService_IsTokenValid_ReturnFalseForExpiredToken() {
        // given
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", -1000L);
        String token = jwtService.generateToken(userDetails);


        // when
        assertThatThrownBy(() -> jwtService.isTokenValid(token, userDetails)).isInstanceOf(ExpiredJwtException.class);
//        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // then
//        assertThat(isValid).isFalse();
    }

}
