<template>
  <div class="dashboard-container">
    <div class="welcome">欢迎，{{ name }}</div>
    <el-row :gutter="16">
      <el-col v-for="item in stats" :key="item.label" :xs="12" :sm="8" :md="6" :lg="3">
        <el-card v-loading="loading" shadow="never" class="stat-card">
          <div class="stat-value">{{ item.value }}</div>
          <div class="stat-label">{{ item.label }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import userApi from '@/api/system/sysUser'
import roleApi from '@/api/system/sysRole'
import menuApi from '@/api/system/sysMenu'
import deptApi from '@/api/system/sysDept'
import postApi from '@/api/system/sysPost'
import loginLogApi from '@/api/system/sysLoginLog'
import operLogApi from '@/api/system/sysOperLog'

export default {
  name: 'Dashboard',
  data() {
    return {
      loading: false,
      stats: [
        { label: '用户', value: 0 },
        { label: '角色', value: 0 },
        { label: '菜单', value: 0 },
        { label: '部门', value: 0 },
        { label: '岗位', value: 0 },
        { label: '登录日志', value: 0 },
        { label: '操作日志', value: 0 }
      ]
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  created() {
    this.loadStats()
  },
  methods: {
    loadStats() {
      this.loading = true
      Promise.all([
        userApi.getPageList(1, 1, {}),
        roleApi.findPage(1, 1, {}),
        menuApi.findNodes(),
        deptApi.findNodes(),
        postApi.findPage(1, 1, {}),
        loginLogApi.findPage(1, 1, {}),
        operLogApi.findPage(1, 1, {})
      ]).then(response => {
        this.stats[0].value = response[0].data.total
        this.stats[1].value = response[1].data.total
        this.stats[2].value = this.countTree(response[2].data)
        this.stats[3].value = this.countTree(response[3].data)
        this.stats[4].value = response[4].data.total
        this.stats[5].value = response[5].data.total
        this.stats[6].value = response[6].data.total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    countTree(nodes) {
      const list = nodes || []
      return list.reduce((total, node) => total + 1 + this.countTree(node.children), 0)
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
}

.welcome {
  margin-bottom: 16px;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}

.stat-card {
  margin-bottom: 16px;
  border-color: #ebeef5;
}

.stat-value {
  color: #303133;
  font-size: 26px;
  font-weight: 600;
  line-height: 1.2;
}

.stat-label {
  margin-top: 6px;
  color: #909399;
  font-size: 13px;
}
</style>
