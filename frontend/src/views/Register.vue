<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2 class="register-title">注册账号</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px" status-icon>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" prefix-icon="el-icon-user" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" prefix-icon="el-icon-smile" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" show-password prefix-icon="el-icon-lock" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" show-password prefix-icon="el-icon-lock" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="submit">注册</el-button>
          <el-link type="info" @click="goToLogin" class="login-link">登录</el-link>
        </el-form-item>
      </el-form>
      <el-alert v-if="msg" :title="msg" :type="msgType" show-icon class="register-msg" />
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ref, reactive } from 'vue'
import axios from 'axios'

const router = useRouter()
const form = reactive({
  username: '',
  nickname: '',
  password: '',
  confirmPassword: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '至少6位', trigger: 'blur' }],
  confirmPassword: [{ required: true, message: '请确认密码', trigger: 'blur' },
    { validator: (rule, value, callback) => {
        if (value !== form.password) callback(new Error('两次密码不一致'))
        else callback()
      }, trigger: 'blur'
    }]
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
      const res = await axios.post('/user/register', {
        username: form.username,
        password: form.password,
        nickname: form.nickname
      })
      if (res.data.code === 200) {
        msg.value = '注册成功！请登录'
        msgType.value = 'success'
        setTimeout(() => {
          router.push('/login')
        }, 800)
      } else {
        msg.value = res.data.message || '注册失败'
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

// 新增：跳转到登录页
const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}
.register-card {
  width: 350px;
  padding: 30px 25px 10px 25px;
  border-radius: 16px;
  box-shadow: 0 2px 16px rgba(100, 170, 255, 0.2);
}
.register-title {
  text-align: center;
  margin-bottom: 20px;
  font-weight: 600;
  color: #3498db;
  letter-spacing: 2px;
}
.register-msg {
  margin-top: 18px;
}
.login-link {
  margin-left: 18px;
  font-size: 13px;
  vertical-align: middle;
  cursor: pointer;
}
</style>