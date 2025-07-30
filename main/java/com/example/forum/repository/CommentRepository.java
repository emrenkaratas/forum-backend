package com.example.forum.repository;

import com.example.forum.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    List<Comment> findByThreadId(Long threadId);
}
