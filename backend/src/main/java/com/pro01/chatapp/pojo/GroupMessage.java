package com.pro01.chatapp.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupMessage {
    private Long id;
    private Long groupId;
    private Long senderId;
    private String content;
    private String type;       // TEXT / IMAGE / VIDEO / FILE
    private String mediaUrl;
    private LocalDateTime sentAt;
}