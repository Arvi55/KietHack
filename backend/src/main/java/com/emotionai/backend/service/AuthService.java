package com.emotionai.backend.service;

import com.emotionai.backend.dto.LoginRequest;
import com.emotionai.backend.dto.SignupRequest;
import com.emotionai.backend.dto.AuthResponse;
import com.emotionai.backend.model.User;
import com.emotionai.backend.repository.UserRepository;
import com.emotionai.backend.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponse signup(SignupRequest request) {
        Optional<User> existing = userRepository.findByEmail(request.getEmail());
        if (existing.isPresent()) {
            throw new RuntimeException("Email already taken");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(SecurityUtil.hashPassword(request.getPassword()));
        
        user = userRepository.save(user);
        return new AuthResponse(user.getId(), user.getName(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
                
        String hashedReqPassword = SecurityUtil.hashPassword(request.getPassword());
        if (!user.getPassword().equals(hashedReqPassword)) {
            throw new RuntimeException("Invalid email or password");
        }
        
        return new AuthResponse(user.getId(), user.getName(), user.getEmail());
    }
}
