<template>
  <div class="chatroom-container">
    <el-card class="chatroom-card">
      <div class="chatroom-header">
        <el-button type="text" @click="goBack">&lt; 返回</el-button>
        <span class="chatroom-title">{{ groupName }}</span>
      </div>
      <div class="chat-history" ref="scrollRef">
        <div
            v-for="msg in messages"
            :key="msg.id || msg.localId"
            :class="['msg-item', msg.senderId === userId ? 'my-msg' : 'other-msg']"
        >
          <!-- 头像和昵称 -->
          <img
              class="avatar"
              :src="msg.senderAvatar || defaultAvatar"
              alt="avatar"
          />
          <div class="msg-content-wrap">
            <div class="nickname">{{ msg.senderNickname }}</div>
            <div class="msg-content">
              <span>{{ msg.content }}</span>
            </div>
            <div class="msg-time">{{ msg.sentAt }}</div>
          </div>
        </div>
        <div v-if="loading" class="loading-tip">消息加载中...</div>
        <div v-if="!loading && messages.length === 0" class="empty-tip">暂无聊天记录</div>
      </div>
      <!-- 消息发送框 -->
      <div class="send-bar">
        <el-input
            v-model="inputMsg"
            :disabled="!wsConnected"
            @keyup.enter="sendMsg"
            placeholder="输入消息回车发送"
            clearable
        />
        <el-button type="primary" @click="sendMsg" :disabled="!inputMsg || !wsConnected" style="margin-left:8px;">
          发送
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()
const groupId = route.params.groupId
const groupName = route.query.name || '群聊'
const pageSize = 20

const userId = Number(localStorage.getItem('userId'))
const nickname = localStorage.getItem('nickname') || '我'
const defaultAvatar = new URL('@/assets/default_avatar.png', import.meta.url).href

const messages = ref([])
const total = ref(0)
const loading = ref(false)
const scrollRef = ref(null)
const inputMsg = ref('')
const ws = ref(null)
const wsConnected = ref(false)

const fetchMessages = async () => {
  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get(`/group/message/${groupId}`, {
      params: { page: 1, pageSize },
      headers: { Authorization: token }
    })
    if (res.data.code === 200) {
      messages.value = res.data.data.records.map(m => ({
        ...m,
        senderAvatar: m.senderAvatar || defaultAvatar
      }))
      total.value = res.data.data.total
      await nextTick()
      scrollToBottom()
    }
  } finally {
    loading.value = false
  }
}

// 滚动到底部
const scrollToBottom = () => {
  if (scrollRef.value) {
    scrollRef.value.scrollTop = scrollRef.value.scrollHeight
  }
}

// WebSocket 相关
const initWebSocket = () => {
  const token = localStorage.getItem('token')
  if (!token) return
  const wsUrl = `ws://192.168.0.134:8080/ws/chat?token=${token}`
  ws.value = new WebSocket(wsUrl)
  ws.value.onopen = () => {
    wsConnected.value = true
    // console.log('ws connected')
  }
  ws.value.onclose = () => {
    wsConnected.value = false
    // console.log('ws closed')
  }
  ws.value.onerror = () => {
    wsConnected.value = false
    // console.log('ws error')
  }
  ws.value.onmessage = (evt) => {
    try {
      const msg = JSON.parse(evt.data)
      // 这里的msg结构和历史接口一致，直接push
      messages.value.push({
        ...msg,
        senderAvatar: msg.senderAvatar || defaultAvatar
      })
      nextTick(scrollToBottom)
    } catch (e) {
      // 可能是心跳或其他数据
    }
  }
}

// 发送消息
const sendMsg = () => {
  if (!inputMsg.value.trim() || !wsConnected.value) return
  const msg = {
    groupId: Number(groupId),
    type: "TEXT",
    content: inputMsg.value,
    mediaUrl: null
  }
  ws.value.send(JSON.stringify(msg))
  // 可以本地先添加一条“临时消息”，待服务端回包覆盖/补全
  // 本地显示更流畅，适合体验

  /**
   * messages.value.push({
   *     localId: `local-${Date.now()}`,
   *     groupId: Number(groupId),
   *     senderId: userId,
   *     senderUsername: localStorage.getItem('username'),
   *     senderNickname: nickname,
   *     senderAvatar: defaultAvatar,
   *     content: inputMsg.value,
   *     type: "TEXT",
   *     mediaUrl: null,
   *     sentAt: new Date().toLocaleString().replace(/\//g, "-").replace(/:\d{2}$/, ""), // 简单时间
   *   })
   *
   */

  nextTick(scrollToBottom)
  inputMsg.value = ''
}

const goBack = () => {
  router.back()
}

onMounted(() => {
  fetchMessages()
  initWebSocket()
})

onBeforeUnmount(() => {
  if (ws.value) ws.value.close()
})
</script>

<style scoped>
.chatroom-container {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(120deg, #fbc2eb 0%, #a6c1ee 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}
.chatroom-card {
  width: 540px;
  min-height: 600px;
  max-height: 90vh;
  border-radius: 16px;
  padding: 0 16px 16px 16px;
  box-shadow: 0 2px 20px rgba(100, 170, 255, 0.18);
  display: flex;
  flex-direction: column;
}
.chatroom-header {
  display: flex;
  align-items: center;
  height: 52px;
  border-bottom: 1px solid #ececec;
  font-weight: 600;
  font-size: 18px;
  color: #34495e;
  margin-bottom: 2px;
}
.chatroom-title {
  flex: 1;
  text-align: center;
  letter-spacing: 1px;
}
.chat-history {
  flex: 1;
  min-height: 0;         /* 关键：防止flex塌陷！ */
  max-height: 60vh;      /* 或根据需要调整，保证区域不会无限变高 */
  overflow-y: auto;      /* 允许滚动 */
  padding: 12px 0 12px 0;
  background: transparent;
}
.send-bar {
  display: flex;
  align-items: center;
  margin-top: 10px;
  gap: 8px;
}
.msg-item {
  display: flex;
  margin-bottom: 18px;
  align-items: flex-end;
  max-width: 86%;
}
.my-msg {
  flex-direction: row-reverse;
  margin-left: auto;
}
.other-msg {
  margin-right: auto;
}
.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  margin: 0 10px;
  background: #fff;
  border: 1px solid #eee;
}
.msg-content-wrap {
  max-width: 320px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.my-msg .msg-content-wrap {
  align-items: flex-end;
}
.nickname {
  font-size: 13px;
  color: #8e99a4;
  margin-bottom: 2px;
  word-break: break-all;
}
.msg-content {
  background: #f4f8fb;
  color: #333;
  padding: 8px 16px;
  border-radius: 16px;
  font-size: 15px;
  margin-bottom: 4px;
  word-break: break-all;
  max-width: 100%;
}
.my-msg .msg-content {
  background: #a1c4fd;
  color: #34495e;
}
.msg-time {
  font-size: 12px;
  color: #bbb;
  margin-top: 0;
}
.loading-tip,
.empty-tip {
  text-align: center;
  color: #aaa;
  margin-top: 32px;
}
</style>