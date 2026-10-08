<template>
  <div class="app-container">
    <el-form :inline="true" size="small" class="filter-form">
      <el-form-item label="模块标题">
        <el-input v-model="searchObj.title" clearable placeholder="请输入模块标题" />
      </el-form-item>
      <el-form-item label="操作人员">
        <el-input v-model="searchObj.operName" clearable placeholder="请输入操作人员" />
      </el-form-item>
      <el-form-item label="操作时间">
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
      <el-table-column prop="title" label="模块标题" min-width="140" />
      <el-table-column prop="businessType" label="业务类型" width="100" />
      <el-table-column prop="requestMethod" label="请求方式" width="100" />
      <el-table-column prop="operName" label="操作人员" min-width="120" />
      <el-table-column prop="operIp" label="主机地址" min-width="130" />
      <el-table-column label="状态" width="90" align="center">
        <template slot-scope="scope">
          <el-tag size="mini" :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '正常' : '异常' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="operTime" label="操作时间" width="170" />
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

    <el-dialog title="操作日志详情" :visible.sync="dialogVisible" width="720px">
      <el-form label-width="90px" size="small">
        <el-form-item label="模块标题">{{ detail.title }}</el-form-item>
        <el-form-item label="业务类型">{{ detail.businessType }}</el-form-item>
        <el-form-item label="请求方式">{{ detail.requestMethod }}</el-form-item>
        <el-form-item label="请求URL">{{ detail.operUrl }}</el-form-item>
        <el-form-item label="操作人员">{{ detail.operName }}</el-form-item>
        <el-form-item label="主机地址">{{ detail.operIp }}</el-form-item>
        <el-form-item label="请求参数">{{ detail.operParam }}</el-form-item>
        <el-form-item label="返回参数">{{ detail.jsonResult }}</el-form-item>
        <el-form-item label="错误消息">{{ detail.errorMsg }}</el-form-item>
        <el-form-item label="操作时间">{{ detail.operTime }}</el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import api from '@/api/system/sysOperLog'

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
