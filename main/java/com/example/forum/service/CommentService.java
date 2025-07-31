package com.example.forum.service;

import com.example.forum.dto.CommentRequest;
import com.example.forum.dto.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(CommentRequest req);
    List<CommentResponse> getAllComments();
    List<CommentResponse> getCommentsByThread(Long threadId);
    CommentResponse updateComment(Long commentId, CommentRequest req);
    void deleteComment(Long commentId);
}
