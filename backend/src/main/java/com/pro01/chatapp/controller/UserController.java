package com.pro01.chatapp.controller;

import com.pro01.chatapp.dto.LoginRequest;
import com.pro01.chatapp.dto.LoginResponse;
import com.pro01.chatapp.dto.RegisterRequest;
import com.pro01.chatapp.pojo.Result;
import com.pro01.chatapp.pojo.User;
import com.pro01.chatapp.service.UserService;
import com.pro01.chatapp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 注册
    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterRequest request) {
        userService.register(request.getUsername(), request.getPassword(), request.getNickname());
        return Result.success();
    }

    // 登录
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getUsername(), request.getPassword());

        // 生成 token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("nickname", user.getNickname());
        String token = JwtUtil.genToken(claims);

        // 返回登录响应信息
        LoginResponse loginResponse = new LoginResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                user.getAvatar() // 可以为 null
        );

        return Result.success(loginResponse);
    }
}