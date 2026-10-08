<template>
  <div class="app-container">
    <!--查询表单-->
    <div class="search-div">
      <el-form label-width="70px" size="small">
        <el-row>
          <el-col :span="24">
            <el-form-item label="角色名称">
              <el-input v-model="searchObj.roleName" style="width: 100%" placeholder="角色名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="display:flex">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="fetchData()">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetData">重置</el-button>
        </el-row>
      </el-form>
    </div>
    <!-- 工具条 -->
    <div class="tools-div">
      <el-button type="success" icon="el-icon-plus" size="mini" @click="add">添 加</el-button>
      <el-button class="btn-add" size="mini" @click="batchRemove()">批量删除</el-button>
    </div>
    <!-- 表格 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      stripe
      border
      style="width: 100%;margin-top: 10px;"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" />
      <el-table-column
        label="序号"
        width="70"
        align="center"
      >
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="roleCode" label="角色编码" />
      <el-table-column prop="description" label="描述" show-overflow-tooltip />
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="280" align="center">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" size="mini" title="修改" @click="edit(scope.row.id)" />
          <el-button type="success" icon="el-icon-s-check" size="mini" title="分配权限" @click="showAssign(scope.row)" />
          <el-button type="danger" icon="el-icon-delete" size="mini" title="删除" @click="removeDataById(scope.row.id)" />
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper"
      @current-change="fetchData"
    />
    <!-- 添加或修改的表单 -->
    <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%">
      <el-form ref="dataForm" :model="sysRole" label-width="150px" size="small" style="padding-right: 40px;">
        <el-form-item label="角色名称">
          <el-input v-model="sysRole.roleName" />
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="sysRole.roleCode" />
        </el-form-item>
        <el-form-item label="角色描述">
          <el-input v-model="sysRole.description" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" icon="el-icon-refresh-right" @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" icon="el-icon-check" size="small" @click="saveOrUpdate()">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="分配权限" :visible.sync="menuDialogVisible" width="520px">
      <el-tree
        ref="menuTree"
        :data="menuTree"
        show-checkbox
        node-key="id"
        default-expand-all
        :props="{ label: 'name', children: 'children' }"
      />
      <span slot="footer">
        <el-button size="small" @click="menuDialogVisible = false">取消</el-button>
        <el-button type="primary" size="small" @click="submitAssign">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import api from '@/api/system/sysRole'
import menuApi from '@/api/system/sysMenu'
export default {
  // 定义数据模型
  data() {
    return {
      listLoading: true, // 数据是否正在加载
      list: [], // 角色列表
      total: 0, // 总记录数
      page: 1, // 页码
      limit: 10, // 每页记录数
      sysRole: {}, // 角色对象
      dialogVisible: false, // 弹窗是否可见
      searchObj: {}, // 查询条件
      idList: [], // 批量删除的id集合
      menuDialogVisible: false,
      menuTree: [],
      currentRoleId: null
    }
  },
  // 页面渲染之前获取数据
  created() {
    this.fetchData()
  },
  // 定义方法
  methods: {
    fetchData(currentPage = 1) {
      this.page = currentPage
      api.findPage(this.page, this.limit, this.searchObj).then(res => {
        this.list = res.data.records
        this.total = res.data.total
        this.listLoading = false
      })
    },
    removeDataById(sysRoleId) {
      this.$confirm('此操作将永久删除该角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.removeById(sysRoleId).then(res => {
          if (res.code) {
            this.$message.success(res.message)
            this.fetchData(this.page)
          } else {
            this.$message.error(res.message)
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    add() {
      this.sysRole = {}
      this.idList = []
      this.dialogVisible = true
    },
    saveOrUpdate() {
      if (this.sysRole.id) {
        this.update()
      } else {
        this.save()
      }
    },
    save() {
      api.save(this.sysRole).then(res => {
        this.$message.success('添加成功')
        this.dialogVisible = false
        this.fetchData(this.page)
      })
    },

    update() {
      api.update(this.sysRole).then(res => {
        this.$message.success('修改成功')
        this.dialogVisible = false
        this.fetchData(this.page)
      })
    },
    resetData() {
      this.sysRole = {}
      this.idList = []
      this.fetchData()
    },
    edit(sysRoleId) {
      this.dialogVisible = true
      api.getRoleId(sysRoleId).then(res => {
        this.sysRole = res.data
      })
    },
    batchRemove() {
      this.$confirm('此操作将永久删除该角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.batchRemove(this.idList).then(res => {
          this.$message.success('删除成功')
          this.fetchData(this.page)
          this.resetData()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    handleSelectionChange(selection) {
      this.idList = selection.map(item => item.id)
    },
    showAssign(row) {
      this.currentRoleId = row.id
      menuApi.toAssign(row.id).then(response => {
        this.menuTree = response.data || []
        this.menuDialogVisible = true
        this.$nextTick(() => {
          this.$refs.menuTree.setCheckedKeys(this.collectSelectedMenuIds(this.menuTree))
        })
      })
    },
    collectSelectedMenuIds(nodes) {
      const result = []
      const children = nodes || []
      children.forEach(node => {
        if (node.isSelect) {
          result.push(node.id)
        }
        result.push(...this.collectSelectedMenuIds(node.children))
      })
      return result
    },
    submitAssign() {
      const checkedKeys = this.$refs.menuTree.getCheckedKeys()
      const halfCheckedKeys = this.$refs.menuTree.getHalfCheckedKeys()
      menuApi.doAssign({
        roleId: this.currentRoleId,
        menuIdList: checkedKeys.concat(halfCheckedKeys)
      }).then(() => {
        this.menuDialogVisible = false
        this.$message.success('权限分配成功')
      })
    }
  }
}
</script>
