package com.example.forum.controller;

import com.example.forum.dto.ThreadRequest;
import com.example.forum.dto.ThreadResponse;
import com.example.forum.service.ThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/threads")
@RequiredArgsConstructor
public class ThreadController {

    private final ThreadService threadService;

    @PostMapping
    public ResponseEntity<ThreadResponse> createThread(@RequestBody ThreadRequest req) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(threadService.createThread(req));
    }

    @GetMapping
    public List<ThreadResponse> getAllThreads() {
        return threadService.getAllThreads();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThreadResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(threadService.getThreadById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        threadService.deleteThread(id);
        return ResponseEntity.noContent().build();
    }
}
