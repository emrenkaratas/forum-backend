package com.example.forum.service;

import com.example.forum.dto.ThreadRequest;
import com.example.forum.dto.ThreadResponse;

import java.util.List;

public interface ThreadService {

    ThreadResponse createThread(ThreadRequest req);

    List<ThreadResponse> getAllThreads();

    ThreadResponse getThreadById(Long id);

    ThreadResponse updateThread(Long id, ThreadRequest req);

    void deleteThread(Long id);
}
