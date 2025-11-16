package com.pro01.chatapp.mapper;

import com.pro01.chatapp.pojo.ChatGroup;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatGroupMapper {

    // 用户创建群聊
    @Insert("INSERT INTO chat_groups(name, description, created_by, created_at) " +
            "VALUES(#{name}, #{description}, #{createdBy}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ChatGroup group);

    // 查看群聊列表
    @Select("SELECT * FROM chat_groups ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<ChatGroup> findAll(@Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM chat_groups")
    long countAll();

    @Select("SELECT group_id FROM group_members WHERE user_id = #{userId}")
    List<Long> findGroupIdsByUser(@Param("userId") Long userId);
}