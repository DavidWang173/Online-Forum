<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2 class="login-title">登录</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px" status-icon>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" prefix-icon="el-icon-user" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" show-password prefix-icon="el-icon-lock" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="submit">登录</el-button>
          <el-link type="info" @click="goToRegister" class="register-link">注册账号</el-link>
        </el-form-item>
      </el-form>
      <el-alert v-if="msg" :title="msg" :type="msgType" show-icon class="login-msg" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const form = reactive({
  username: '',
  password: ''
})
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}
const formRef = ref(null)
const loading = ref(false)
const msg = ref('')
const msgType = ref('')

const submit = () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true
    msg.value = ''
    try {
      const res = await axios.post('/user/login', {
        username: form.username,
        password: form.password
      })
      if (res.data.code === 200) {
        // 存储 token
        localStorage.setItem('token', res.data.data.token)
        localStorage.setItem('userId', res.data.data.userId)
        localStorage.setItem('username', res.data.data.username)
        localStorage.setItem('nickname', res.data.data.nickname)
        localStorage.setItem('avatar', res.data.data.avatar ?? '')
        msg.value = '登录成功，正在跳转...'
        msgType.value = 'success'
        setTimeout(() => {
          router.push('/home') // 跳转到你的主页面路径
        }, 800)
      } else {
        msg.value = res.data.message || '登录失败'
        msgType.value = 'error'
      }
    } catch (e) {
      msg.value = '网络异常，请重试'
      msgType.value = 'error'
    } finally {
      loading.value = false
    }
  })
}

// 新增：跳转注册页
const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}
.login-card {
  width: 350px;
  padding: 30px 25px 10px 25px;
  border-radius: 16px;
  box-shadow: 0 2px 16px rgba(100, 170, 255, 0.2);
}
.login-title {
  text-align: center;
  margin-bottom: 20px;
  font-weight: 600;
  color: #3498db;
  letter-spacing: 2px;
}
.login-msg {
  margin-top: 18px;
}
.register-link {
  margin-left: 18px;
  font-size: 13px;
  vertical-align: middle;
  cursor: pointer;
}
</style>