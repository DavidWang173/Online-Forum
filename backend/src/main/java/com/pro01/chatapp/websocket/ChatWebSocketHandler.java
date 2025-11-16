package com.pro01.chatapp.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pro01.chatapp.dto.GroupMessageDTO;
import com.pro01.chatapp.dto.GroupMessageRequest;
import com.pro01.chatapp.service.GroupMessageService;
import com.pro01.chatapp.utils.JwtUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ObjectMapper objectMapper;

    private final GroupMessageService groupMessageService;

    // 群组ID -> 用户连接列表
    private final Map<Long, Set<WebSocketSession>> groupSessions = new ConcurrentHashMap<>();

    public ChatWebSocketHandler(GroupMessageService groupMessageService) {
        this.groupMessageService = groupMessageService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = getTokenFromQueryParams(session);
        Map<String, Object> claims = JwtUtil.parseToken(token);

        if (claims == null || !claims.containsKey("userId")) {
            session.close();
            return;
        }

        Long userId = ((Number) claims.get("userId")).longValue();
        session.getAttributes().put("userId", userId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long userId = (Long) session.getAttributes().get("userId");

        // 1. 解析客户端消息
        GroupMessageRequest request = objectMapper.readValue(message.getPayload(), GroupMessageRequest.class);
        request.setSenderId(userId);

        // 2. 存入数据库 + 获取完整 DTO（含头像、昵称）
        GroupMessageDTO fullMessage = groupMessageService.saveGroupMessage(request);

        // 3. 保存当前连接到群组映射中
        groupSessions.computeIfAbsent(request.getGroupId(), k -> ConcurrentHashMap.newKeySet())
                .add(session);

        // 4. 转为 JSON
        String json = objectMapper.writeValueAsString(fullMessage);

        // 5. 广播到群组中所有在线用户
        for (WebSocketSession ws : groupSessions.get(request.getGroupId())) {
            if (ws.isOpen()) {
                ws.sendMessage(new TextMessage(json));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        for (Set<WebSocketSession> sessions : groupSessions.values()) {
            sessions.remove(session);
        }
    }

    /**
     * 从 WebSocket 握手 URI 的查询参数中解析 token，如 ws://localhost:8080/ws/chat?token=xxx
     */
    private String getTokenFromQueryParams(WebSocketSession session) {
        String query = session.getUri().getQuery(); // 例如 token=abc123
        if (query != null) {
            for (String param : query.split("&")) {
                String[] parts = param.split("=");
                if (parts.length == 2 && parts[0].equals("token")) {
                    return parts[1];
                }
            }
        }
        return null;
    }
}