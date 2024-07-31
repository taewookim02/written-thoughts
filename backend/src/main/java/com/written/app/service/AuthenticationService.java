package com.written.app.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.written.app.dto.AuthenticationRequest;
import com.written.app.dto.AuthenticationResponse;
import com.written.app.dto.RegisterRequest;
import com.written.app.exception.PasswordMismatchException;
import com.written.app.exception.UserAlreadyExistsException;
import com.written.app.model.Role;
import com.written.app.model.Token;
import com.written.app.model.TokenType;
import com.written.app.model.User;
import com.written.app.repository.TokenRepository;
import com.written.app.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request, HttpServletResponse response) {
        // Check if user exists or not
        // FIXME: redundant?
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }


        // Check if password, confirmPassword matches
        System.out.println("request = " + request);
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("Password and confirm password do not match");
        }


        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nick("") // default
                .isDeleted(false) // default
                .createdAt(LocalDateTime.now()) // default
                .role(Role.USER)
                .build();
        var savedUser = userRepository.save(user);

        // generate access, refresh tokens
        var jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateRefreshToken(savedUser);


        // save access token in db
        saveUserToken(savedUser, jwtToken);
        // set refreshToken in response via cookie
        addRefreshTokenCookie(response, refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
//                .refreshToken(refreshToken) // adding refreshToken in the http only cookie
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request,
                                               HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(); // TODO: throw precise exception and handle

        // generate access, refresh tokens
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // revoke all the tokens of a user first
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        addRefreshTokenCookie(response, refreshToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        if (validUserTokens.isEmpty()) {
            return;
        }

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
        System.out.println("Cookie added: " + cookie.getName() +
                ", Value: " + cookie.getValue() +
                ", HttpOnly: " + cookie.isHttpOnly() +
//                ", Secure: " + cookie.getSecure() +
                ", MaxAge: " + cookie.getMaxAge());
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request,
                                               HttpServletResponse response) throws IOException {

        final String refreshToken = extractRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            return null;
        }
        System.out.println("refreshToken = " + refreshToken);

        final String userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var userDetails = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();


            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                var accessToken = jwtService.generateToken(userDetails);

                // revoke other tokens and save new token
                revokeAllUserTokens(userDetails);
                saveUserToken(userDetails, accessToken);


               var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .build();


               return authResponse;
            }
        }
        return null;
    }
}
