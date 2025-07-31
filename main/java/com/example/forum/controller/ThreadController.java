package com.example.forum.controller;

import com.example.forum.dto.ThreadRequest;
import com.example.forum.model.Thread;
import com.example.forum.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/threads")
public class ThreadController {

    @Autowired private ThreadService threadService;

    @PostMapping
    public ResponseEntity<Thread> createThread(@RequestBody ThreadRequest req) {
        Thread created = threadService.createThread(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Thread> getAllThreads() {
        return threadService.getAllThreads();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Thread> getThreadById(@PathVariable Long id) {
        Thread t = threadService.getThreadById(id);
        return ResponseEntity.ok(t);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Thread> updateThread(
            @PathVariable Long id,
            @RequestBody ThreadRequest req
    ) {
        Thread updated = threadService.updateThread(id, req);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteThread(@PathVariable Long id) {
        threadService.deleteThread(id);
        return ResponseEntity.noContent().build();
    }
}
