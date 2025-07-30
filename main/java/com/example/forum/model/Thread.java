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
    }

    public Thread(String title, String content, User user, User insertedBy, User updatedBy) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.insertedBy = insertedBy;
        this.updatedBy = updatedBy;
        this.createdAt = LocalDateTime.now();
    }


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
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
    }

    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
