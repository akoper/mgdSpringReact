package com.example.mgdSpringReact.web;

import com.example.mgdSpringReact.model.UserAccount;
import com.example.mgdSpringReact.repository.UserAccountRepository;
import com.example.mgdSpringReact.security.JwtService;
import com.example.mgdSpringReact.web.dto.AuthDtos.AuthResponse;
import com.example.mgdSpringReact.web.dto.AuthDtos.LoginRequest;
import com.example.mgdSpringReact.web.dto.AuthDtos.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public AuthController(UserAccountRepository users, PasswordEncoder passwordEncoder,
                          AuthenticationManager authManager, JwtService jwtService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        String username = request.getUsername().trim().toLowerCase();
        if (username.isEmpty()) {
            return ResponseEntity.badRequest().body("username required");
        }
        if (users.existsByUsername(username)) {
            return ResponseEntity.status(409).body("username already exists");
        }
        UserAccount ua = new UserAccount();
        ua.setUsername(username);
        ua.setPassword(passwordEncoder.encode(request.getPassword()));
        ua.setRoles("USER");
        UserAccount saved = users.save(ua);

        String token = jwtService.generateToken(saved.getUsername(), Map.of("roles", saved.getRoles()));
        return ResponseEntity.created(URI.create("/api/users/" + saved.getId()))
                .body(new AuthResponse(token, saved.getUsername(), saved.getRoles()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        String username = auth.getName();
        var ua = users.findByUsername(username).orElseThrow();
        String token = jwtService.generateToken(username, Map.of("roles", ua.getRoles()));
        return ResponseEntity.ok(new AuthResponse(token, ua.getUsername(), ua.getRoles()));
    }
}
