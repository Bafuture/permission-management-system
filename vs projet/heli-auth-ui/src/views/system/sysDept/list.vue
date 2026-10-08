<template>
  <div class="app-container">
    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" size="mini" @click="add">新增部门</el-button>
      <el-button icon="el-icon-refresh" size="mini" @click="fetchData">刷新</el-button>
    </div>

    <el-table
      v-loading="listLoading"
      :data="list"
      row-key="id"
      border
      default-expand-all
      :tree-props="{ children: 'children' }"
    >
      <el-table-column prop="name" label="部门名称" min-width="200" />
      <el-table-column prop="leader" label="负责人" min-width="120" />
      <el-table-column prop="phone" label="联系电话" min-width="140" />
      <el-table-column prop="sortValue" label="排序" width="80" align="center" />
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

    <el-dialog title="部门信息" :visible.sync="dialogVisible" width="560px">
      <el-form ref="dataForm" :model="sysDept" label-width="90px" size="small">
        <el-form-item label="上级部门">
          <el-select v-model="sysDept.parentId" style="width: 100%">
            <el-option label="顶级部门" :value="0" />
            <el-option
              v-for="item in parentOptions"
              :key="item.id"
              :label="item.label"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="部门名称">
          <el-input v-model="sysDept.name" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="sysDept.leader" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="sysDept.phone" />
        </el-form-item>
        <el-form-item label="排序值">
          <el-input-number v-model="sysDept.sortValue" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="sysDept.status">
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
import api from '@/api/system/sysDept'

const defaultForm = {
  id: null,
  parentId: 0,
  name: '',
  leader: '',
  phone: '',
  sortValue: 1,
  status: 1
}

export default {
  data() {
    return {
      listLoading: false,
      list: [],
      dialogVisible: false,
      sysDept: Object.assign({}, defaultForm)
    }
  },
  computed: {
    parentOptions() {
      return this.flattenTree(this.list)
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      api.findNodes().then(response => {
        this.list = response.data || []
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    flattenTree(nodes, level = 0) {
      const result = []
      const children = nodes || []
      children.forEach(node => {
        if (node.id !== this.sysDept.id) {
          result.push({
            id: node.id,
            label: `${'-- '.repeat(level)}${node.name}`
          })
        }
        result.push(...this.flattenTree(node.children, level + 1))
      })
      return result
    },
    add() {
      this.sysDept = Object.assign({}, defaultForm)
      this.dialogVisible = true
    },
    edit(id) {
      api.getById(id).then(response => {
        this.sysDept = response.data
        this.dialogVisible = true
      })
    },
    saveOrUpdate() {
      const request = this.sysDept.id ? api.update : api.save
      request(this.sysDept).then(() => {
        this.dialogVisible = false
        this.$message.success('保存成功')
        this.fetchData()
      })
    },
    removeDataById(id) {
      this.$confirm('确定删除该部门吗？', '提示', {
        type: 'warning'
      }).then(() => api.removeById(id)).then(() => {
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
    },
    switchStatus(row) {
      api.updateStatus(row.id, row.status).then(() => {
        this.$message.success('状态更新成功')
      }).catch(() => {
        this.fetchData()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.toolbar {
  margin-bottom: 16px;
}

.danger-text {
  color: #f56c6c;
}
</style>
