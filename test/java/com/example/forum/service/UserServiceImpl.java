package com.example.forum.service;

import com.example.forum.dto.LoginRequest;
import com.example.forum.dto.LoginResponse;
import com.example.forum.dto.UserRequest;
import com.example.forum.model.User;
import com.example.forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public User register(UserRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Username already taken");
        }
        User u = User.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .email(req.getEmail())
                .name(req.getName())
                .surname(req.getSurname())
                .birthdate(req.getBirthdate())
                .build();
        return userRepository.save(u);
    }

    @Override
    public LoginResponse login(LoginRequest req) {
        User u = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!passwordEncoder.matches(req.getPassword(), u.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return new LoginResponse("Login successful");
    }

    @Override
    public User updateUser(Long id, UserRequest req) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        existing.setUsername(req.getUsername());
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        existing.setEmail(req.getEmail());
        existing.setName(req.getName());
        existing.setSurname(req.getSurname());
        existing.setBirthdate(req.getBirthdate());
        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }
}
