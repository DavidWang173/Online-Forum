<template>
  <div class="create-group-container">
    <el-card class="create-group-card">
      <h2 class="create-group-title">创建群聊</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px" status-icon>
        <el-form-item label="群聊名称" prop="name">
          <el-input v-model="form.name" maxlength="16" show-word-limit placeholder="请输入群聊名称"/>
        </el-form-item>
        <el-form-item label="简介" prop="description">
          <el-input v-model="form.description" type="textarea" maxlength="64" show-word-limit placeholder="请输入简介"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="submit">创建</el-button>
          <el-button @click="goBack" style="margin-left: 16px;">返回</el-button>
        </el-form-item>
      </el-form>
      <el-alert v-if="msg" :title="msg" :type="msgType" show-icon class="group-msg" />
    </el-card>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ref, reactive } from 'vue'
import axios from 'axios'

const router = useRouter()
const form = reactive({
  name: '',
  description: ''
})

const rules = {
  name: [{ required: true, message: '请输入群聊名称', trigger: 'blur' }],
  description: [{ required: true, message: '请输入简介', trigger: 'blur' }]
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
      const token = localStorage.getItem('token')
      const res = await axios.post('/group/create', {
        name: form.name,
        description: form.description
      }, {
        headers: {
          Authorization: token // 如果不带Bearer就直接用token
        }
      })
      if (res.data.code === 200) {
        msg.value = '创建成功！'
        msgType.value = 'success'
        setTimeout(() => {
          router.push('/home')
        }, 1000)
      } else {
        msg.value = res.data.message || '创建失败'
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
const goBack = () => {
  router.back()
}
</script>

<style scoped>
.create-group-container {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(120deg, #f6d365 0%, #fda085 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}
.create-group-card {
  width: 400px;
  border-radius: 14px;
  box-shadow: 0 2px 16px rgba(100, 170, 255, 0.18);
  padding: 25px 18px 10px 18px;
}
.create-group-title {
  text-align: center;
  margin-bottom: 18px;
  font-weight: 600;
  color: #f57b51;
  letter-spacing: 2px;
}
.group-msg {
  margin-top: 18px;
}
</style>