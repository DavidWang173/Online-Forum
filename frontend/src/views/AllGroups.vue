<template>
  <div class="all-group-container">
    <el-card class="all-group-card">
      <div class="all-group-header">
        <el-button @click="goBack" type="info" size="small">返回主页</el-button>
        <span class="all-group-title">所有群聊</span>
      </div>
      <el-divider />
      <el-table
          :data="groupList"
          stripe
          style="width: 100%"
          v-loading="loading"
          :empty-text="loading ? '加载中...' : '暂无群聊'"
      >
        <el-table-column prop="name" label="群聊名称" />
        <el-table-column prop="description" label="简介" />
        <el-table-column prop="createdAt" label="创建时间" :formatter="formatTime" />
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button
                v-if="!scope.row.joined"
                type="primary"
                size="small"
                @click.stop="joinGroup(scope.row)"
                :disabled="joining[scope.row.id]"
            >
              {{ joining[scope.row.id] ? "加入中..." : "加入" }}
            </el-button>
            <span v-else style="color: #67c23a; font-size: 13px;">已加入</span>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination" v-if="total > pageSize">
        <el-pagination
            background
            layout="prev, pager, next"
            :current-page="page"
            :page-size="pageSize"
            :total="total"
            @current-change="handlePageChange"
        />
      </div>
      <el-alert v-if="msg" :title="msg" :type="msgType" show-icon class="all-group-msg" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const groupList = ref([])
const page = ref(1)
const pageSize = ref(10)
const total = ref(0)
const loading = ref(false)
const msg = ref('')
const msgType = ref('')
const joining = ref({}) // 正在加入的群聊id

const fetchAllGroups = async () => {
  loading.value = true
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('/group/list', {
      params: { page: page.value, pageSize: pageSize.value },
      headers: { Authorization: token }
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

const joinGroup = async (row) => {
  if (joining.value[row.id]) return
  joining.value[row.id] = true
  msg.value = ''
  try {
    const token = localStorage.getItem('token')
    const res = await axios.post(`/group/join/${row.id}`, null, {
      headers: { Authorization: token }
    })
    if (res.data.code === 200) {
      msg.value = `已加入「${row.name}」群聊！`
      msgType.value = 'success'
      // 加入成功后刷新群聊列表
      fetchAllGroups()
    } else {
      msg.value = res.data.message || '加入失败'
      msgType.value = 'error'
    }
  } catch (e) {
    msg.value = '网络异常，请重试'
    msgType.value = 'error'
  } finally {
    joining.value[row.id] = false
  }
}

const handlePageChange = (val) => {
  page.value = val
  fetchAllGroups()
}

const formatTime = (row) => {
  if (!row.createdAt) return ''
  return row.createdAt.replace('T', ' ').substring(0, 16)
}

const goBack = () => {
  router.push('/home')
}

onMounted(fetchAllGroups)
</script>

<style scoped>
.all-group-container {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(120deg, #a1c4fd 0%, #c2e9fb 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}
.all-group-card {
  width: 580px;
  min-height: 420px;
  border-radius: 14px;
  box-shadow: 0 2px 18px rgba(100, 170, 255, 0.18);
  padding: 0 20px 20px 20px;
}
.all-group-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 18px;
}
.all-group-title {
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
.all-group-msg {
  margin-top: 18px;
}
</style>