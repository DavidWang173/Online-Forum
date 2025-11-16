package com.pro01.chatapp.service.impl;

import com.pro01.chatapp.mapper.UserMapper;
import com.pro01.chatapp.pojo.User;
import com.pro01.chatapp.service.UserService;
import com.pro01.chatapp.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(String username, String password, String nickname) {
        if (userMapper.findByUsername(username) != null) {
            throw new RuntimeException("用户名已存在");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(Md5Util.getMD5String(password));
        user.setNickname(nickname);
        userMapper.insert(user);
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null || !Md5Util.checkPassword(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        return user;
    }
}