package com.taskmanagement.controller;

import com.taskmanagement.model.User;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.config.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Default role is USER, but you can set it to ADMIN based on your needs
        userRepository.save(user);
        return Map.of("message", "User registered successfully");
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        User user = userRepository.findByUsername(request.get("username"));
        if (user != null && passwordEncoder.matches(request.get("password"), user.getPassword())) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String token = jwtProvider.generateToken(auth);
            return Map.of("token", token);
        } else {
            return Map.of("error", "Invalid credentials");
        }
    }
}
