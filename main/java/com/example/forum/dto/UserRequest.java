package com.example.forum.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;
    private LocalDate birthdate;
}