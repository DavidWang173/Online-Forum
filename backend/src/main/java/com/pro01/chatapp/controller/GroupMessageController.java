package com.pro01.chatapp.controller;

import com.pro01.chatapp.dto.GroupMessageDTO;
import com.pro01.chatapp.pojo.PageResult;
import com.pro01.chatapp.pojo.Result;
import com.pro01.chatapp.pojo.GroupMessage;
import com.pro01.chatapp.service.GroupMessageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group/message")
public class GroupMessageController {

    private final GroupMessageService groupMessageService;

    public GroupMessageController(GroupMessageService groupMessageService) {
        this.groupMessageService = groupMessageService;
    }

    // 查看群聊历史记录
    @GetMapping("/{groupId}")
    public Result<PageResult<GroupMessageDTO>> getGroupMessages(
            @PathVariable Long groupId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize
    ) {
        PageResult<GroupMessageDTO> result = groupMessageService.getMessagesByGroup(groupId, page, pageSize);
        return Result.success(result);
    }

}