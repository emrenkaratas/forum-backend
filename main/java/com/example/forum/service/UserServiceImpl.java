package com.example.forum.service;

import com.example.forum.model.User;
import com.example.forum.repository.UserRepository;
import com.example.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        // Here you could hash passwords or enforce business rules before saving
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        Optional<User> opt = userRepository.findById(id);
        if (opt.isEmpty()) {
            return null;
        }
        User existing = opt.get();
        existing.setUsername(updatedUser.getUsername());
        existing.setPassword(updatedUser.getPassword());
        existing.setEmail(updatedUser.getEmail());
        existing.setName(updatedUser.getName());
        existing.setSurname(updatedUser.getSurname());
        existing.setBirthdate(updatedUser.getBirthdate());
        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
