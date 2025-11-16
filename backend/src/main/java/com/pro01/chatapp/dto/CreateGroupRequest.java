package com.pro01.chatapp.dto;

import lombok.Data;

@Data
public class CreateGroupRequest {
    private String name;
    private String description;
}