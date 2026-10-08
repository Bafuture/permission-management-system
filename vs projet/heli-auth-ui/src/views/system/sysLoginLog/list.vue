<template>
  <div class="app-container">
    <el-form :inline="true" size="small" class="filter-form">
      <el-form-item label="用户账号">
        <el-input v-model="searchObj.username" clearable placeholder="请输入用户账号" />
      </el-form-item>
      <el-form-item label="登录时间">
        <el-date-picker
          v-model="createTimes"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          value-format="yyyy-MM-dd HH:mm:ss"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="fetchData()">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetData">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="listLoading" :data="list" border stripe>
      <el-table-column type="index" label="序号" width="70" align="center" />
      <el-table-column prop="username" label="用户账号" min-width="130" />
      <el-table-column prop="ipaddr" label="登录IP" min-width="140" />
      <el-table-column label="状态" width="90" align="center">
        <template slot-scope="scope">
          <el-tag size="mini" :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '成功' : '失败' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="msg" label="提示信息" min-width="180" show-overflow-tooltip />
      <el-table-column prop="accessTime" label="访问时间" width="170" />
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="90" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-view" @click="showDetail(scope.row.id)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      :current-page="page"
      :page-size="limit"
      :total="total"
      layout="total, prev, pager, next, jumper"
      class="pagination"
      @current-change="fetchData"
    />

    <el-dialog title="登录日志详情" :visible.sync="dialogVisible" width="620px">
      <el-form label-width="90px" size="small">
        <el-form-item label="用户账号">{{ detail.username }}</el-form-item>
        <el-form-item label="登录IP">{{ detail.ipaddr }}</el-form-item>
        <el-form-item label="状态">{{ detail.status === 1 ? '成功' : '失败' }}</el-form-item>
        <el-form-item label="提示信息">{{ detail.msg }}</el-form-item>
        <el-form-item label="访问时间">{{ detail.accessTime }}</el-form-item>
        <el-form-item label="创建时间">{{ detail.createTime }}</el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import api from '@/api/system/sysLoginLog'

export default {
  data() {
    return {
      listLoading: false,
      list: [],
      total: 0,
      page: 1,
      limit: 10,
      createTimes: [],
      searchObj: {},
      dialogVisible: false,
      detail: {}
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData(page = 1) {
      this.page = page
      if (this.createTimes && this.createTimes.length === 2) {
        this.searchObj.createTimeBegin = this.createTimes[0]
        this.searchObj.createTimeEnd = this.createTimes[1]
      }
      this.listLoading = true
      api.findPage(this.page, this.limit, this.searchObj).then(response => {
        this.list = response.data.records
        this.total = response.data.total
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    resetData() {
      this.searchObj = {}
      this.createTimes = []
      this.fetchData()
    },
    showDetail(id) {
      api.getById(id).then(response => {
        this.detail = response.data
        this.dialogVisible = true
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.filter-form {
  padding: 16px 16px 0;
  background: #fff;
}

.pagination {
  margin-top: 16px;
  text-align: right;
}
</style>
