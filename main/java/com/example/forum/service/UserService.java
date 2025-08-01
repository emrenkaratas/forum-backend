package com.example.forum.service;

import com.example.forum.dto.LoginRequest;
import com.example.forum.dto.LoginResponse;
import com.example.forum.dto.UserRequest;
import com.example.forum.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    User register(UserRequest req);
    LoginResponse login(LoginRequest req);
    User updateUser(Long id, UserRequest req);
    void deleteUser(Long id);
}
