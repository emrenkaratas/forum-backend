// src/main/java/com/example/forum/model/Comment.java
package com.example.forum.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @ManyToOne(optional = false)
    @JoinColumn(name = "thread_id", nullable = false)
    @JsonBackReference
    private Thread thread;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "inserted_by")
    private User insertedBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    public Comment() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public Comment(String content,
                   Thread thread,
                   User user,
                   User insertedBy) {
        this.content = content;
        this.thread = thread;
        this.user = user;
        this.insertedBy = insertedBy;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
        touchUpdatedAt();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Thread getThread() {
        return thread;
    }
    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public User getInsertedBy() {
        return insertedBy;
    }
    public void setInsertedBy(User insertedBy) {
        this.insertedBy = insertedBy;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }
    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
        touchUpdatedAt();
    }

    // internal helper to update timestamp
    private void touchUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}