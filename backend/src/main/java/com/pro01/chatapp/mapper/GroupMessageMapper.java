package com.pro01.chatapp.mapper;

import com.pro01.chatapp.dto.GroupMessageDTO;
import com.pro01.chatapp.pojo.GroupMessage;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GroupMessageMapper {

    @Insert("INSERT INTO group_messages(group_id, sender_id, content, type, media_url, sent_at) " +
            "VALUES(#{groupId}, #{senderId}, #{content}, #{type}, #{mediaUrl}, #{sentAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(GroupMessage message);

    @Select("""
    SELECT gm.id, gm.group_id, gm.sender_id,
           u.username AS sender_username,
           u.nickname AS sender_nickname,
           u.avatar AS sender_avatar,
           gm.content, gm.type, gm.media_url, gm.sent_at
    FROM group_messages gm
    JOIN users u ON gm.sender_id = u.id
    WHERE gm.id = #{id}
""")
    GroupMessageDTO selectDTOById(@Param("id") Long id);

    // 查看聊天记录
    @Select("""
        SELECT id, group_id, sender_id, content, type, media_url, sent_at
        FROM group_messages
        WHERE group_id = #{groupId}
        ORDER BY sent_at ASC 
        LIMIT #{limit} OFFSET #{offset}
    """)
    List<GroupMessage> selectByGroup(@Param("groupId") Long groupId,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM group_messages WHERE group_id = #{groupId}")
    long countByGroup(@Param("groupId") Long groupId);

    @Select("""
    SELECT gm.id, gm.group_id, gm.sender_id,
           u.username AS sender_username,
           u.nickname AS sender_nickname,
           u.avatar AS sender_avatar,   -- ✅ 新增
           gm.content, gm.type, gm.media_url, gm.sent_at
    FROM group_messages gm
    JOIN users u ON gm.sender_id = u.id
    WHERE gm.group_id = #{groupId}
    ORDER BY gm.sent_at ASC 
    LIMIT #{limit} OFFSET #{offset}
""")
    List<GroupMessageDTO> selectDTOByGroup(@Param("groupId") Long groupId,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);
}