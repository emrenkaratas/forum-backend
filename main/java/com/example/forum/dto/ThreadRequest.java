package com.example.forum.dto;

public class ThreadRequest {
    private String title;
    private String content;
    private Long userId;
    private Long insertedById;
    private Long updatedById;

    public ThreadRequest() {}

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
