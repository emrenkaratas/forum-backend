package com.example.forum.service;

import com.example.forum.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User createUser(User user);
    User getUserById(Long id);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
}
