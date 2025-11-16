package com.pro01.chatapp.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatGroup {
    private Long id;
    private String name;
    private String description;
    private Long createdBy;
    private LocalDateTime createdAt;
}