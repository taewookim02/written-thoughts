package com.written.app.auth;

import com.written.app.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://localhost:3000"})
public class AuthenticationController {

    private final AuthenticationService service;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        System.out.println("request = " + request);
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request,
            @RequestHeader("Authorization") String token
    ) {


        return ResponseEntity.ok(service.authenticate(request, token));
    }


}
