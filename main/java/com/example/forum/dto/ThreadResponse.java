package com.example.forum.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ThreadResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int viewCount;
    private Long userId;
    private Long insertedById;
    private Long updatedById;
    private List<CommentSummary> comments;

    @Data
    @NoArgsConstructor
    public static class CommentSummary {
        private Long id;
        private String content;
        private LocalDateTime createdAt;
        private Long userId;
    }
}
