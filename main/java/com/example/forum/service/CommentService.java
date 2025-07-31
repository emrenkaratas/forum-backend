package com.example.forum.service;

import com.example.forum.dto.CommentRequest;
import com.example.forum.model.Comment;

import java.util.List;

public interface CommentService {
    Comment createComment(CommentRequest req);
    List<Comment> getAllComments();
    List<Comment> getCommentsByThread(Long threadId);
}
