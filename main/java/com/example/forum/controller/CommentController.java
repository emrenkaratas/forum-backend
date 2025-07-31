package com.example.forum.controller;

import com.example.forum.dto.CommentRequest;
import com.example.forum.model.Comment;
import com.example.forum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentRequest req) {
        Comment created = commentService.createComment(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }


    @GetMapping("/thread/{threadId}")
    public List<Comment> getCommentsByThread(@PathVariable Long threadId) {
        return commentService.getCommentsByThread(threadId);
    }
}
