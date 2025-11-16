package com.pro01.chatapp.mapper;

import com.pro01.chatapp.pojo.ChatGroup;
import com.pro01.chatapp.pojo.GroupMember;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupMemberMapper {

    // 用户创建群聊
    @Insert("INSERT INTO group_members(group_id, user_id, joined_at) VALUES(#{groupId}, #{userId}, NOW())")
    void insert(GroupMember member);

    // 用户加入群聊
    @Select("SELECT COUNT(*) FROM group_members WHERE group_id = #{groupId} AND user_id = #{userId}")
    boolean exists(@Param("groupId") Long groupId, @Param("userId") Long userId);

    // 用户查看自己加入的群聊
    @Select("""
    SELECT cg.* FROM chat_groups cg
    JOIN group_members gm ON cg.id = gm.group_id
    WHERE gm.user_id = #{userId}
    ORDER BY gm.joined_at DESC
    LIMIT #{offset}, #{limit}
""")
    List<ChatGroup> findByUserId(@Param("userId") Long userId,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    @Select("SELECT COUNT(*) FROM group_members WHERE user_id = #{userId}")
    long countByUserId(@Param("userId") Long userId);
}