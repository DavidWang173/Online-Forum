package com.pro01.chatapp.controller;

import com.pro01.chatapp.dto.ChatGroupDTO;
import com.pro01.chatapp.dto.CreateGroupRequest;
import com.pro01.chatapp.dto.CreateGroupResponse;
import com.pro01.chatapp.pojo.ChatGroup;
import com.pro01.chatapp.pojo.PageResult;
import com.pro01.chatapp.pojo.Result;
import com.pro01.chatapp.service.ChatGroupService;
import com.pro01.chatapp.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/group")
public class ChatGroupController {

    @Autowired
    private ChatGroupService chatGroupService;

    // 用户创建群聊
    @PostMapping("/create")
    public Result<CreateGroupResponse> createGroup(@RequestBody CreateGroupRequest request, HttpServletRequest httpRequest) {
        Long userId = TokenUtil.getUserId(httpRequest);
        Long groupId = chatGroupService.createGroup(userId, request.getName(), request.getDescription());
        return Result.success(new CreateGroupResponse(groupId));
    }

    // 查看群聊列表
    @GetMapping("/list")
    public Result<PageResult<ChatGroupDTO>> listAllGroups(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpServletRequest request) {

        Long userId = TokenUtil.getUserId(request); // ✅ 获取当前登录用户
        PageResult<ChatGroupDTO> result = chatGroupService.listAllGroups(pageNum, pageSize, userId);
        return Result.success(result);
    }

    // 用户加入群聊
    @PostMapping("/join/{groupId}")
    public Result<Void> joinGroup(@PathVariable Long groupId, HttpServletRequest request) {
        Long userId = TokenUtil.getUserId(request);
        chatGroupService.joinGroup(userId, groupId);
        return Result.success();
    }

    // 用户查看自己加入的群聊
    @GetMapping("/my")
    public Result<PageResult<ChatGroup>> listMyGroups(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpServletRequest request) {

        Long userId = TokenUtil.getUserId(request);
        PageResult<ChatGroup> result = chatGroupService.listUserGroups(userId, pageNum, pageSize);
        return Result.success(result);
    }
}