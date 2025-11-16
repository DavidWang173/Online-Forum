package com.pro01.chatapp.service;

import com.pro01.chatapp.pojo.User;

public interface UserService {
    void register(String username, String password, String nickname);
    User login(String username, String password);
}