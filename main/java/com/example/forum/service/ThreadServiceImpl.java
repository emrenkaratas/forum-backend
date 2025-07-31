package com.example.forum.service;

import com.example.forum.dto.ThreadRequest;
import com.example.forum.dto.ThreadResponse;
import com.example.forum.dto.ThreadResponse.CommentSummary;
import com.example.forum.model.Thread;
import com.example.forum.model.User;
import com.example.forum.repository.ThreadRepository;
import com.example.forum.repository.UserRepository;
import com.example.forum.service.ThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThreadServiceImpl implements ThreadService {

    private final ThreadRepository threadRepository;
    private final UserRepository   userRepository;

    @Override
    public ThreadResponse createThread(ThreadRequest req) {
        User author = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        User insBy = userRepository.findById(req.getInsertedById())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "InsertedBy not found"));
        User updBy = req.getUpdatedById() == null
                ? null
                : userRepository.findById(req.getUpdatedById())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "UpdatedBy not found"));

        Thread thread = new Thread();
        thread.setTitle(req.getTitle());
        thread.setContent(req.getContent());
        thread.setUser(author);
        thread.setInsertedBy(insBy);
        thread.setUpdatedBy(updBy);


        Thread saved = threadRepository.save(thread);
        return mapToDto(saved);
    }

    @Override
    public List<ThreadResponse> getAllThreads() {
        return threadRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ThreadResponse getThreadById(Long id) {
        Thread thread = threadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found"));

        thread.incrementViewCount();
        threadRepository.save(thread);
        return mapToDto(thread);
    }

    @Override
    public ThreadResponse updateThread(Long id, ThreadRequest req) {
        Thread existing = threadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found"));

        existing.setTitle(req.getTitle());
        existing.setContent(req.getContent());
        if (req.getUpdatedById() != null) {
            User updBy = userRepository.findById(req.getUpdatedById())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "UpdatedBy not found"));
            existing.setUpdatedBy(updBy);
        }

        Thread saved = threadRepository.save(existing);
        return mapToDto(saved);
    }

    @Override
    public void deleteThread(Long id) {
        if (!threadRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found");
        }
        threadRepository.deleteById(id);
    }

    private ThreadResponse mapToDto(Thread t) {
        ThreadResponse dto = new ThreadResponse();
        dto.setId(t.getId());
        dto.setTitle(t.getTitle());
        dto.setContent(t.getContent());
        dto.setCreatedAt(t.getCreatedAt());
        dto.setUpdatedAt(t.getUpdatedAt());
        dto.setViewCount(t.getViewCount());
        dto.setUserId(t.getUser().getId());
        dto.setInsertedById(t.getInsertedBy() != null ? t.getInsertedBy().getId() : null);
        dto.setUpdatedById(t.getUpdatedBy() != null ? t.getUpdatedBy().getId() : null);
        dto.setComments(
                t.getComments().stream().map(c -> {
                    CommentSummary cs = new CommentSummary();
                    cs.setId(c.getId());
                    cs.setContent(c.getContent());
                    cs.setCreatedAt(c.getCreatedAt());
                    cs.setUserId(c.getUser().getId());
                    return cs;
                }).collect(Collectors.toList())
        );
        return dto;
    }
}
