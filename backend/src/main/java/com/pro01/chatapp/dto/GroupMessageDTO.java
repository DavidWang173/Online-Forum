package com.pro01.chatapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessageDTO {
    private Long id;
    private Long groupId;
    private Long senderId;
    private String senderUsername;
    private String senderNickname;
    private String senderAvatar; // ✅ 新增字段
    private String content;
    private String type;
    private String mediaUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sentAt;
}