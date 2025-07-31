package com.example.forum.controller;

import com.example.forum.dto.CommentRequest;
import com.example.forum.dto.CommentResponse;
import com.example.forum.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CommentRequest req) {
        CommentResponse created = commentService.createComment(req);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }


    @GetMapping
    public List<CommentResponse> getAllComments() {
        return commentService.getAllComments();
    }


    @GetMapping("/thread/{threadId}")
    public List<CommentResponse> getCommentsByThread(@PathVariable Long threadId) {
        return commentService.getCommentsByThread(threadId);
    }


    @PutMapping("/{id}")
    public CommentResponse updateComment(
            @PathVariable Long id,
            @RequestBody CommentRequest req
    ) {
        return commentService.updateComment(id, req);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}

