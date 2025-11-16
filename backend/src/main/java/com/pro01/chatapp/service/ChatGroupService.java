package com.pro01.chatapp.service;
import com.pro01.chatapp.dto.ChatGroupDTO;
import com.pro01.chatapp.pojo.ChatGroup;
import com.pro01.chatapp.pojo.PageResult;


public interface ChatGroupService {
    // 用户创建群聊
    Long createGroup(Long userId, String name, String description);

    // 查看群聊列表
    PageResult<ChatGroupDTO> listAllGroups(int pageNum, int pageSize, Long userId);

    // 用户加入群聊
    void joinGroup(Long userId, Long groupId);

    // 用户查看自己加入的群聊
    PageResult<ChatGroup> listUserGroups(Long userId, int pageNum, int pageSize);
}