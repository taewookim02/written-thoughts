package com.written.app.service;

import com.written.app.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private String secretKey = "secretKeySecretKeySecretKeySecretKeySecretKeySecretKeySecretKeySecretKeySecretKey";

    @BeforeEach
    public void setUp() {
        // @Value in jwt
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);
        ReflectionTestUtils.setField(jwtService, "refreshExpiration", 604800000L);
    }

    @Test
    public void JwtService_ExtractUsername_ReturnUsername() {
        // given
        String username = "test@example.com";
        UserDetails userDetails = User.builder()
                .email(username)
                .password("password")
                .tokens(new ArrayList<>())
                .build();
        String token = jwtService.generateToken(userDetails);


        // when
        String extractedUsername = jwtService.extractUsername(token);

        // then
        assertThat(extractedUsername).isEqualTo(username);
    }


}