package com.pro01.chatapp.mapper;

import com.pro01.chatapp.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("INSERT INTO users(username, password, nickname, created_at) " +
            "VALUES(#{username}, #{password}, #{nickname}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);
}