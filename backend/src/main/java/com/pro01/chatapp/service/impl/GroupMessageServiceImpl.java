package com.pro01.chatapp.service.impl;

import com.pro01.chatapp.dto.GroupMessageDTO;
import com.pro01.chatapp.dto.GroupMessageRequest;
import com.pro01.chatapp.mapper.GroupMessageMapper;
import com.pro01.chatapp.pojo.GroupMessage;
import com.pro01.chatapp.pojo.PageResult;
import com.pro01.chatapp.service.GroupMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupMessageServiceImpl implements GroupMessageService {

    @Autowired
    private GroupMessageMapper groupMessageMapper;

    @Override
    public GroupMessageDTO saveGroupMessage(GroupMessageRequest request) {
        GroupMessage msg = new GroupMessage();
        msg.setGroupId(request.getGroupId());
        msg.setSenderId(request.getSenderId());
        msg.setType(request.getType());
        msg.setContent(request.getContent());
        msg.setMediaUrl(request.getMediaUrl());
        msg.setSentAt(LocalDateTime.now());

        groupMessageMapper.insert(msg); // 自动回填 msg.id

        return groupMessageMapper.selectDTOById(msg.getId());
    }

    @Override
    public PageResult<GroupMessageDTO> getMessagesByGroup(Long groupId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<GroupMessageDTO> list = groupMessageMapper.selectDTOByGroup(groupId, offset, pageSize);
        long total = groupMessageMapper.countByGroup(groupId);
        return new PageResult<>(total, list);
    }
}