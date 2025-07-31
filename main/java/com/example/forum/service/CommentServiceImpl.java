package com.example.forum.service;

import com.example.forum.dto.CommentRequest;
import com.example.forum.dto.CommentResponse;
import com.example.forum.model.Comment;
import com.example.forum.model.Thread;
import com.example.forum.model.User;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.ThreadRepository;
import com.example.forum.repository.UserRepository;
import com.example.forum.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ThreadRepository  threadRepository;
    private final UserRepository    userRepository;

    @Override
    public CommentResponse createComment(CommentRequest req) {
        Thread thread = threadRepository.findById(req.getThreadId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thread not found"));

        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        User insertedBy = userRepository.findById(req.getInsertedById())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "InsertedBy user not found"));

        User updatedBy = null;
        if (req.getUpdatedById() != null) {
            updatedBy = userRepository.findById(req.getUpdatedById())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "UpdatedBy user not found"));
        }

        Comment comment = new Comment();
        comment.setContent(req.getContent());
        comment.setThread(thread);
        comment.setUser(user);
        comment.setInsertedBy(insertedBy);
        comment.setUpdatedBy(updatedBy);

        Comment saved = commentRepository.save(comment);
        return mapToDto(saved);
    }

    @Override
    public CommentResponse updateComment(Long commentId, CommentRequest req) {
        Comment existing = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        existing.setContent(req.getContent());

        if (req.getUpdatedById() != null) {
            User upd = userRepository.findById(req.getUpdatedById())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "UpdatedBy user not found"));
            existing.setUpdatedBy(upd);
        }

        Comment saved = commentRepository.save(existing);
        return mapToDto(saved);
    }

    @Override
    public List<CommentResponse> getAllComments() {
        return commentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getCommentsByThread(Long threadId) {
        return commentRepository.findByThreadId(threadId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }
        commentRepository.deleteById(commentId);
    }

    private CommentResponse mapToDto(Comment c) {
        CommentResponse dto = new CommentResponse();
        dto.setId(c.getId());
        dto.setContent(c.getContent());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setUpdatedAt(c.getUpdatedAt());
        dto.setThreadId(c.getThread().getId());
        dto.setUserId(c.getUser().getId());
        dto.setInsertedById(c.getInsertedBy() != null ? c.getInsertedBy().getId() : null);
        dto.setUpdatedById(c.getUpdatedBy()  != null ? c.getUpdatedBy().getId()  : null);
        return dto;
    }
}
