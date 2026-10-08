<template>
  <div class="app-container">
    <el-form :inline="true" size="small" class="filter-form">
      <el-form-item label="岗位编码">
        <el-input v-model="searchObj.postCode" clearable placeholder="请输入岗位编码" />
      </el-form-item>
      <el-form-item label="岗位名称">
        <el-input v-model="searchObj.name" clearable placeholder="请输入岗位名称" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="fetchData()">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetData">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" size="mini" @click="add">新增</el-button>
    </div>

    <el-table v-loading="listLoading" :data="list" border stripe>
      <el-table-column type="index" label="序号" width="70" align="center" />
      <el-table-column prop="postCode" label="岗位编码" min-width="140" />
      <el-table-column prop="name" label="岗位名称" min-width="140" />
      <el-table-column prop="description" label="描述" min-width="180" show-overflow-tooltip />
      <el-table-column label="状态" width="90" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            @change="switchStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-edit" @click="edit(scope.row.id)">修改</el-button>
          <el-button type="text" icon="el-icon-delete" class="danger-text" @click="removeDataById(scope.row.id)">删除</el-button>
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

    <el-dialog title="岗位信息" :visible.sync="dialogVisible" width="520px">
      <el-form ref="dataForm" :model="sysPost" label-width="90px" size="small">
        <el-form-item label="岗位编码">
          <el-input v-model="sysPost.postCode" />
        </el-form-item>
        <el-form-item label="岗位名称">
          <el-input v-model="sysPost.name" />
        </el-form-item>
        <el-form-item label="岗位描述">
          <el-input v-model="sysPost.description" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="sysPost.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="small" @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" size="small" @click="saveOrUpdate">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import api from '@/api/system/sysPost'

const defaultForm = {
  id: null,
  postCode: '',
  name: '',
  description: '',
  status: 1
}

export default {
  data() {
    return {
      listLoading: false,
      list: [],
      total: 0,
      page: 1,
      limit: 10,
      searchObj: {},
      dialogVisible: false,
      sysPost: Object.assign({}, defaultForm)
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData(page = 1) {
      this.page = page
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
      this.fetchData()
    },
    add() {
      this.sysPost = Object.assign({}, defaultForm)
      this.dialogVisible = true
    },
    edit(id) {
      api.getById(id).then(response => {
        this.sysPost = response.data
        this.dialogVisible = true
      })
    },
    saveOrUpdate() {
      if (this.sysPost.id) {
        api.update(this.sysPost).then(() => {
          this.dialogVisible = false
          this.$message.success('修改成功')
          this.fetchData(this.page)
        })
      } else {
        api.save(this.sysPost).then(() => {
          this.dialogVisible = false
          this.$message.success('新增成功')
          this.fetchData()
        })
      }
    },
    removeDataById(id) {
      this.$confirm('确定删除该岗位吗？', '提示', {
        type: 'warning'
      }).then(() => api.removeById(id)).then(() => {
        this.$message.success('删除成功')
        this.fetchData(this.page)
      }).catch(() => {})
    },
    switchStatus(row) {
      api.updateStatus(row.id, row.status).then(() => {
        this.$message.success('状态更新成功')
      }).catch(() => {
        this.fetchData(this.page)
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

.toolbar {
  padding: 16px;
  background: #fff;
}

.pagination {
  margin-top: 16px;
  text-align: right;
}

.danger-text {
  color: #f56c6c;
}
</style>
