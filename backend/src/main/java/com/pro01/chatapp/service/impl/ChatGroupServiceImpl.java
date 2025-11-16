package com.pro01.chatapp.service.impl;

import com.pro01.chatapp.dto.ChatGroupDTO;
import com.pro01.chatapp.mapper.ChatGroupMapper;
import com.pro01.chatapp.mapper.GroupMemberMapper;
import com.pro01.chatapp.pojo.ChatGroup;
import com.pro01.chatapp.pojo.GroupMember;
import com.pro01.chatapp.pojo.PageResult;
import com.pro01.chatapp.service.ChatGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatGroupServiceImpl implements ChatGroupService {

    @Autowired
    private ChatGroupMapper chatGroupMapper;

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    // 用户创建群聊
    @Override
    public Long createGroup(Long userId, String name, String description) {
        ChatGroup group = new ChatGroup();
        group.setName(name);
        group.setDescription(description);
        group.setCreatedBy(userId);
        chatGroupMapper.insert(group);

        GroupMember member = new GroupMember();
        member.setGroupId(group.getId());
        member.setUserId(userId);
        groupMemberMapper.insert(member);

        return group.getId();
    }

    // 查看群聊列表
    @Override
    public PageResult<ChatGroupDTO> listAllGroups(int pageNum, int pageSize, Long userId) {
        int offset = (pageNum - 1) * pageSize;
        List<ChatGroup> groups = chatGroupMapper.findAll(offset, pageSize);
        long total = chatGroupMapper.countAll();

        List<Long> joinedGroupIds = chatGroupMapper.findGroupIdsByUser(userId);

        List<ChatGroupDTO> dtoList = groups.stream().map(group -> {
            ChatGroupDTO dto = new ChatGroupDTO();
            dto.setId(group.getId());
            dto.setName(group.getName());
            dto.setDescription(group.getDescription());
            dto.setCreatedBy(group.getCreatedBy());
            dto.setCreatedAt(group.getCreatedAt());
            dto.setJoined(joinedGroupIds.contains(group.getId()));
            return dto;
        }).toList();

        return new PageResult<>(total, dtoList);
    }

    // 用户加入群聊
    @Override
    public void joinGroup(Long userId, Long groupId) {
        boolean alreadyJoined = groupMemberMapper.exists(groupId, userId);
        if (alreadyJoined) {
            throw new RuntimeException("您已加入该群聊");
        }

        GroupMember member = new GroupMember();
        member.setGroupId(groupId);
        member.setUserId(userId);
        groupMemberMapper.insert(member);
    }

    // 用户查看自己加入的群聊
    @Override
    public PageResult<ChatGroup> listUserGroups(Long userId, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<ChatGroup> groups = groupMemberMapper.findByUserId(userId, offset, pageSize);
        long total = groupMemberMapper.countByUserId(userId);
        return new PageResult<>(total, groups);
    }
}