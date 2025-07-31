package com.example.forum.service;

import com.example.forum.dto.CommentRequest;
import com.example.forum.model.Comment;
import com.example.forum.model.Thread;
import com.example.forum.model.User;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.ThreadRepository;
import com.example.forum.repository.UserRepository;
import com.example.forum.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired private CommentRepository commentRepository;
    @Autowired private ThreadRepository threadRepository;
    @Autowired private UserRepository userRepository;

    @Override
    public Comment createComment(CommentRequest req) {
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

        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Long id, CommentRequest req) {
        Comment existing = commentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Comment not found"));


        existing.setContent(req.getContent());
        if (req.getUpdatedById() != null) {
            User upd = userRepository.findById(req.getUpdatedById())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "UpdatedBy user not found"));
            existing.setUpdatedBy(upd);
        }

        return commentRepository.save(existing);
    }

    @Override
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> getCommentsByThread(Long threadId) {
        return commentRepository.findByThreadId(threadId);
    }
}
