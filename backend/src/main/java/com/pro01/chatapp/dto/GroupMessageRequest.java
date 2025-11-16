package com.pro01.chatapp.dto;

import lombok.Data;

@Data
public class GroupMessageRequest {
    private Long groupId;
    private String type;       // TEXT, IMAGE, VIDEO, etc.
    private String content;
    private String mediaUrl;

    // 服务端补充
    private Long senderId;
}