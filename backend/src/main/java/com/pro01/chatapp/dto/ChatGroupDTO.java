package com.pro01.chatapp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatGroupDTO {
    private Long id;
    private String name;
    private String description;
    private Long createdBy;
    private LocalDateTime createdAt;

    private Boolean joined; // ✅ 当前用户是否已加入该群
}