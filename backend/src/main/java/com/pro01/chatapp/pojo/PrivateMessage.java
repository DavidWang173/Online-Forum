package com.pro01.chatapp.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrivateMessage {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private String type;       // TEXT / IMAGE / VIDEO / FILE
    private String mediaUrl;
    private LocalDateTime sentAt;
    private Boolean isRead;
}