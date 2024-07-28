package com.written.app.service;

import com.written.app.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        // then
        assertThatThrownBy(() -> jwtService.isTokenValid(token, userDetails)).isInstanceOf(ExpiredJwtException.class);
//        boolean isValid = jwtService.isTokenValid(token, userDetails);
//        assertThat(isValid).isFalse();
    }

    @Test
    public void JwtService_GenerateRefreshToken_ReturnRefreshToken() {
        // given

        // when
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        // then
        assertThat(refreshToken).isNotBlank();
        assertThat(jwtService.extractUsername(refreshToken)).isEqualTo(username);
    }


    @Test
    public void JwtService_ExtractClaim_ReturnExtractSubject() {
        // given
        String subject = "test@example.com";
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", subject);
        String token = jwtService.generateToken(claims, userDetails);

        // when
        String extractedSubject = jwtService.extractClaim(token, Claims::getSubject);

        // then
        assertThat(extractedSubject).isEqualTo(subject);
    }




}
