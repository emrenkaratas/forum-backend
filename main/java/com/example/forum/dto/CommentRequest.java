package com.example.forum.dto;

public class CommentRequest {
    private String content;
    private Long threadId;
    private Long userId;
    private Long insertedById;
    private Long updatedById;

    public CommentRequest() {}

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public Long getThreadId() {
        return threadId;
    }
    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getInsertedById() {
        return insertedById;
    }
    public void setInsertedById(Long insertedById) {
        this.insertedById = insertedById;
    }

    public Long getUpdatedById() {
        return updatedById;
    }
    public void setUpdatedById(Long updatedById) {
        this.updatedById = updatedById;
    }
}
