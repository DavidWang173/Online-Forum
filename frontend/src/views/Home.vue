<template>
  <div class="home-container">
    <el-card class="home-card">
      <div class="home-header">
        <el-button type="primary" size="small" @click="goCreate">创建群聊</el-button>
        <span class="home-title">我的群聊</span>
        <el-button type="info" size="small" @click="goAllGroups">所有群聊</el-button>
      </div>
      <el-divider />
      <el-table
          :data="groupList"
          stripe
          style="width: 100%"
          @row-click="goToChat"
          v-loading="loading"
          :empty-text="loading ? '加载中...' : '你还没有加入任何群聊~'"
      >
        <el-table-column prop="name" label="群聊名称" />
        <el-table-column prop="description" label="简介" />
        <el-table-column prop="createdAt" label="创建时间"
                         :formatter="formatTime"
        />
      </el-table>
      <div class="pagination" v-if="total > pageSize">
        <el-pagination
            background
            layout="prev, pager, next"
            :current-page="pageNum"
            :page-size="pageSize"
            :total="total"
            @current-change="handlePageChange"
        />
      </div>
    </el-card>
    <!-- 右下角退出按钮 -->
    <el-button class="logout-btn" type="danger" size="small" @click="logout">
      退出登录
    </el-button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const groupList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)

const fetchMyGroups = async () => {
  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/group/my', {
      params: { pageNum: pageNum.value, pageSize: pageSize.value },
      headers: {
        Authorization: token
      }
    })
    if (res.data.code === 200) {
      groupList.value = res.data.data.records || []
      total.value = res.data.data.total || 0
    } else {
      groupList.value = []
      total.value = 0
    }
  } catch (e) {
    groupList.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const goCreate = () => {
  router.push('/group/create')
}
const goAllGroups = () => {
  router.push('/group/all')
}
const goToChat = (row) => {
  router.push({
    path: `/chat/${row.id}`,
    query: { name: row.name }
  })
}

const handlePageChange = (val) => {
  pageNum.value = val
  fetchMyGroups()
}
const formatTime = (row) => {
  if (!row.createdAt) return ''
  return row.createdAt.replace('T', ' ').substring(0, 16)
}

// 退出登录
const logout = () => {
  localStorage.clear()
  router.replace('/login')
}

onMounted(() => {
  fetchMyGroups()
})
</script>

<style scoped>
.home-container {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(120deg, #fbc2eb 0%, #a6c1ee 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}
.home-card {
  width: 540px;
  min-height: 420px;
  border-radius: 14px;
  box-shadow: 0 2px 18px rgba(100, 170, 255, 0.18);
  padding: 0 20px 20px 20px;
}
.home-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 18px;
}
.home-title {
  font-size: 20px;
  font-weight: 600;
  color: #34495e;
  letter-spacing: 1px;
}
.pagination {
  margin-top: 18px;
  display: flex;
  justify-content: center;
}

/* 右下角退出按钮样式 */
.logout-btn {
  position: fixed;
  right: 36px;
  bottom: 32px;
  z-index: 1001;
  box-shadow: 0 2px 10px rgba(255, 65, 65, 0.13);
}
</style>