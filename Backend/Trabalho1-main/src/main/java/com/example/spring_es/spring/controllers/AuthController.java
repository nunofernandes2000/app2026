package com.example.spring_es.spring.controllers;

import com.example.spring_es.spring.domain.User;
import com.example.spring_es.spring.dto.AuthRequest;
import com.example.spring_es.spring.dto.AuthResponse;
import com.example.spring_es.spring.repository.UserRepository;
import com.example.spring_es.spring.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        User u = userRepository.findByEmail(request.getEmail());

        if (u == null) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }

        if (!passwordEncoder.matches(request.getPassword(), u.getPassword())) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }

        Long userId = ((Integer) u.getId()).longValue(); // ✅ Integer -> Long

        // ✅ roles vêm da BD via query nativa
        List<String> roles = userRepository.findRoleNamesByEmail(u.getEmail());

        if (roles == null || roles.isEmpty()) {
            return ResponseEntity.status(500).body("Role não encontrado para o utilizador.");
        }

        String token = jwtService.generateToken(u.getEmail(), userId, roles);

        AuthResponse response = new AuthResponse();
        response.setUserId(userId);
        response.setEmail(u.getEmail());
        response.setNome(resolveNome(u));
        response.setRole(normalizeRoleForFrontend(roles.get(0)));
        response.setToken(token);

        return ResponseEntity.ok(response);
    }

    private String resolveNome(User u) {
        // tenta getNome()
        try {
            return (String) u.getClass().getMethod("getNome").invoke(u);
        } catch (Exception ignored) {}

        // tenta getName()
        try {
            return (String) u.getClass().getMethod("getName").invoke(u);
        } catch (Exception ignored) {}

        // fallback
        return u.getEmail();
    }

    private String normalizeRoleForFrontend(String role) {
        if (role == null) return "";
        if (role.startsWith("ROLE_")) return role.substring(5);
        return role;
    }
}
