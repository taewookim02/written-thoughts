package com.written.app.service;


import com.written.app.dto.AuthenticationRequest;
import com.written.app.dto.AuthenticationResponse;
import com.written.app.dto.RegisterRequest;
import com.written.app.model.Role;
import com.written.app.model.User;
import com.written.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nick("") // default
                .isDeleted(false) // default
                .createdDate(LocalDateTime.now()) // default
                .role(Role.USER)
                .build();


        System.out.println("user = " + user);

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request, String token) {
        // extract token
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
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(); // TODO: throw precise exception and handle

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
