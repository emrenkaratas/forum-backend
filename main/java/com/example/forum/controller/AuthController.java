package com.example.forum.controller;

import com.example.forum.dto.LoginRequest;
import com.example.forum.dto.LoginResponse;
import com.example.forum.dto.UserRequest;
import com.example.forum.model.User;
import com.example.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRequest req) {
        User created = userService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        LoginResponse resp = userService.login(req);
        return ResponseEntity.ok(resp);
    }
}
