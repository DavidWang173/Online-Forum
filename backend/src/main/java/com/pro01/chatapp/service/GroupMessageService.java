package com.pro01.chatapp.service;

import com.pro01.chatapp.dto.GroupMessageDTO;
import com.pro01.chatapp.dto.GroupMessageRequest;
import com.pro01.chatapp.pojo.GroupMessage;
import com.pro01.chatapp.pojo.PageResult;

public interface GroupMessageService {

    // 聊天记录写入数据库
    GroupMessageDTO saveGroupMessage(GroupMessageRequest request);

    // 查看聊天记录
    PageResult<GroupMessageDTO> getMessagesByGroup(Long groupId, int page, int pageSize);

}