// src/main/java/com/example/forum/service/impl/ThreadServiceImpl.java
package com.example.forum.service;

import com.example.forum.dto.ThreadRequest;
import com.example.forum.model.Thread;
import com.example.forum.model.User;
import com.example.forum.repository.ThreadRepository;
import com.example.forum.repository.UserRepository;
import com.example.forum.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ThreadServiceImpl implements ThreadService {

    @Autowired
    private ThreadRepository threadRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Thread createThread(ThreadRequest req) {
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

        return threadRepository.save(thread);
    }

    @Override
    public List<Thread> getAllThreads() {
        return threadRepository.findAll();
    }

    @Override
    public Thread getThreadById(Long id) {
        Thread t = threadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found"));
        // increment viewCount on each fetch
        t.incrementViewCount();
        return threadRepository.save(t);
    }

    @Override
    public Thread updateThread(Long id, ThreadRequest req) {
        Thread existing = threadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found"));

        existing.setTitle(req.getTitle());
        existing.setContent(req.getContent());

        if (req.getUpdatedById() != null) {
            User updBy = userRepository.findById(req.getUpdatedById())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "UpdatedBy not found"));
            existing.setUpdatedBy(updBy);
        }
        return threadRepository.save(existing);
    }

    @Override
    public void deleteThread(Long id) {
        if (!threadRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found");
        }
        threadRepository.deleteById(id);
    }
}
