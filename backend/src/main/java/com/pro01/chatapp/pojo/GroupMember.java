package com.pro01.chatapp.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupMember {
    private Long id;
    private Long groupId;
    private Long userId;
    private LocalDateTime joinedAt;
}