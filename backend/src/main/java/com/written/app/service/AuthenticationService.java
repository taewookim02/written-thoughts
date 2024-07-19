package com.written.app.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.written.app.dto.AuthenticationRequest;
import com.written.app.dto.AuthenticationResponse;
import com.written.app.dto.RegisterRequest;
import com.written.app.model.Role;
import com.written.app.model.Token;
import com.written.app.model.TokenType;
import com.written.app.model.User;
import com.written.app.repository.TokenRepository;
import com.written.app.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        // TODO: Check if user exists or not


        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nick("") // default
                .isDeleted(false) // default
                .createdAt(LocalDateTime.now()) // default
                .role(Role.USER)
                .build();
        var savedUser = repository.save(user);

        // generate access, refresh tokens
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }



    public AuthenticationResponse authenticate(AuthenticationRequest request
//            , String token
    ) {
        /*// extract token
        String reqjwtToken = token.substring(7);
        System.out.println("jwtToken = " + reqjwtToken);

        // extract email
        String tokenEmail = jwtService.extractUsername(reqjwtToken);
        System.out.println("tokenEmail = " + tokenEmail);

        // req email
        String reqEmail = request.getEmail();
        System.out.println("reqEmail = " + reqEmail);

        // Check if the email from the token matches the email in the request body
        if (!tokenEmail.equals(reqEmail)) {
            throw new IllegalArgumentException("Unauthorized: Email does not match the token");
        }*/

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(); // TODO: throw precise exception and handle

        // generate access, refresh tokens
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // revoke all the tokens of a user first
        revokeAllUserTokens(user);

        // then save
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
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

    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7); // exclude Bearer

        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var userDetails = this.repository.findByEmail(userEmail)
                    .orElseThrow();

            // check if token valid in db
            /*var isTokenValid = tokenRepository.findByToken(refreshToken)
                    .map(token -> !token.isExpired() && !token.isRevoked())
                    .orElse(false);*/

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
               var accessToken = jwtService.generateToken(userDetails);
               // revoke other tokens and save new token
               revokeAllUserTokens(userDetails);
               saveUserToken(userDetails, accessToken);

               var authResponse = AuthenticationResponse.builder()
                       .accessToken(accessToken)
                       .refreshToken(refreshToken)
                       .build();

               // write value in response
               new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
