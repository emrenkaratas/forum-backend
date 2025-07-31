package com.example.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ThreadRequest {
    private String title;
    private String content;
    private Long userId;
    private Long insertedById;
    private Long updatedById;
}
