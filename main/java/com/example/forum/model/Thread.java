package com.example.forum.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "threads")
public class Thread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private int viewCount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "inserted_by")
    private User insertedBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Thread() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.viewCount = 0;
    }

    public Thread(String title,
                  String content,
                  User user,
                  User insertedBy,
                  User updatedBy) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.insertedBy = insertedBy;
        this.updatedBy = updatedBy;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.viewCount = 0;
    }

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
        touchUpdatedAt();
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

    public int getViewCount() {
        return viewCount;
    }
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
    public void incrementViewCount() {
        this.viewCount++;
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

    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    private void touchUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}