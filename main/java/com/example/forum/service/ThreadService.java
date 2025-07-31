package com.example.forum.service;

import com.example.forum.dto.ThreadRequest;
import com.example.forum.model.Thread;

import java.util.List;

public interface ThreadService {
    Thread createThread(ThreadRequest req);
    List<Thread> getAllThreads();
    Thread getThreadById(Long id);
    Thread updateThread(Long id, ThreadRequest req);
    void deleteThread(Long id);
}
