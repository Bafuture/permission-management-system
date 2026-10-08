<template>
  <div class="app-container">
    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" size="mini" @click="add">新增菜单</el-button>
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
      <el-table-column prop="name" label="菜单名称" min-width="180" />
      <el-table-column label="类型" width="90" align="center">
        <template slot-scope="scope">
          <el-tag size="mini" :type="scope.row.type === 1 ? 'success' : 'info'">
            {{ scope.row.type === 1 ? '菜单' : '按钮' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="path" label="路由地址" min-width="150" />
      <el-table-column prop="component" label="组件路径" min-width="180" />
      <el-table-column prop="perms" label="权限标识" min-width="160" />
      <el-table-column prop="icon" label="图标" min-width="120" />
      <el-table-column prop="sortValue" label="排序" width="80" align="center" />
      <el-table-column label="状态" width="90" align="center">
        <template slot-scope="scope">
          <el-tag size="mini" :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '正常' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-edit" @click="edit(scope.row.id)">修改</el-button>
          <el-button type="text" icon="el-icon-delete" class="danger-text" @click="removeDataById(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="菜单信息" :visible.sync="dialogVisible" width="620px">
      <el-form ref="dataForm" :model="sysMenu" label-width="100px" size="small">
        <el-form-item label="上级菜单">
          <el-select v-model="sysMenu.parentId" style="width: 100%">
            <el-option label="顶级菜单" :value="0" />
            <el-option
              v-for="item in parentOptions"
              :key="item.id"
              :label="item.label"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="菜单名称">
          <el-input v-model="sysMenu.name" />
        </el-form-item>
        <el-form-item label="菜单类型">
          <el-radio-group v-model="sysMenu.type">
            <el-radio :label="1">菜单</el-radio>
            <el-radio :label="2">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="路由地址">
          <el-input v-model="sysMenu.path" />
        </el-form-item>
        <el-form-item label="组件路径">
          <el-input v-model="sysMenu.component" />
        </el-form-item>
        <el-form-item label="权限标识">
          <el-input v-model="sysMenu.perms" />
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="sysMenu.icon" />
        </el-form-item>
        <el-form-item label="排序值">
          <el-input-number v-model="sysMenu.sortValue" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="sysMenu.status">
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
import api from '@/api/system/sysMenu'

const defaultForm = {
  id: null,
  parentId: 0,
  name: '',
  type: 1,
  path: '',
  component: '',
  perms: '',
  icon: '',
  sortValue: 1,
  status: 1
}

export default {
  data() {
    return {
      listLoading: false,
      list: [],
      dialogVisible: false,
      sysMenu: Object.assign({}, defaultForm)
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
        if (node.type === 1 && node.id !== this.sysMenu.id) {
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
      this.sysMenu = Object.assign({}, defaultForm)
      this.dialogVisible = true
    },
    edit(id) {
      api.getById(id).then(response => {
        this.sysMenu = response.data
        this.dialogVisible = true
      })
    },
    saveOrUpdate() {
      const request = this.sysMenu.id ? api.update : api.save
      request(this.sysMenu).then(() => {
        this.dialogVisible = false
        this.$message.success('保存成功')
        this.fetchData()
      })
    },
    removeDataById(id) {
      this.$confirm('确定删除该菜单吗？', '提示', {
        type: 'warning'
      }).then(() => api.removeById(id)).then(() => {
        this.$message.success('删除成功')
        this.fetchData()
      }).catch(() => {})
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
