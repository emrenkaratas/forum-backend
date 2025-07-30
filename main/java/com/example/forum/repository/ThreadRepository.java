package com.example.forum.repository;

import com.example.forum.model.Thread;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreadRepository extends JpaRepository<Thread, Long> {
}
