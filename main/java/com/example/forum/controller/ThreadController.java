package com.example.forum.controller;

import com.example.forum.dto.ThreadRequest;
import com.example.forum.model.Thread;
import com.example.forum.model.User;
import com.example.forum.repository.ThreadRepository;
import com.example.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/threads")
public class ThreadController {

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Thread> createThread(@RequestBody ThreadRequest req) {

        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "User not found"));

        User insertedBy = userRepository.findById(req.getInsertedById())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "InsertedBy user not found"));

        User updatedBy = null;
        if (req.getUpdatedById() != null) {
            updatedBy = userRepository.findById(req.getUpdatedById())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "UpdatedBy user not found"));
        }


        Thread thread = new Thread();
        thread.setTitle(req.getTitle());
        thread.setContent(req.getContent());
        thread.setUser(user);
        thread.setInsertedBy(insertedBy);
        thread.setUpdatedBy(updatedBy);



        Thread saved = threadRepository.save(thread);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Thread> getAllThreads() {
        return threadRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Thread> getThreadById(@PathVariable Long id) {
        return threadRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThread(@PathVariable Long id) {
        if (!threadRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        threadRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
