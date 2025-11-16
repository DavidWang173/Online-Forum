package com.pro01.chatapp.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}