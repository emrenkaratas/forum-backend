
package com.example.forum.controller;

import com.example.forum.dto.CommentRequest;
import com.example.forum.model.Comment;
import com.example.forum.model.Thread;
import com.example.forum.model.User;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.ThreadRepository;
import com.example.forum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ThreadRepository threadRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentRequest req) {

        Thread thread = threadRepository.findById(req.getThreadId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Thread not found"));


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


        Comment comment = new Comment();
        comment.setContent(req.getContent());
        comment.setThread(thread);
        comment.setUser(user);
        comment.setInsertedBy(insertedBy);
        comment.setUpdatedBy(updatedBy);


        Comment saved = commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @GetMapping("/thread/{threadId}")
    public List<Comment> getCommentsByThread(@PathVariable Long threadId) {
        return commentRepository.findByThreadId(threadId);
    }
}
