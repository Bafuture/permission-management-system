<template>
  <div class="app-container">

    <div class="search-div">
      <el-form label-width="70px" size="small">
        <el-row>
          <el-col :span="8">
            <el-form-item label="关 键 字">
              <el-input v-model="searchObj.keyword" style="width: 95%" placeholder="用户名/姓名/手机号码" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="操作时间">
              <el-date-picker
                v-model="createTimes"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="margin-right: 10px;width: 100%;"
              />
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
    </div>

    <!-- 列表 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      stripe
      border
      style="width: 100%;margin-top: 10px;"
    >

      <el-table-column
        label="序号"
        width="70"
        align="center"
      >
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="username" label="用户名" width="180" />
      <el-table-column prop="name" label="姓名" width="110" />
      <el-table-column prop="phone" label="手机" />
      <el-table-column label="部门" min-width="140">
        <template slot-scope="scope">{{ getDeptName(scope.row.deptId) }}</template>
      </el-table-column>
      <el-table-column label="岗位" min-width="120">
        <template slot-scope="scope">{{ getPostName(scope.row.postId) }}</template>
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="1"
            :inactive-value="0"
            @change="switchStatus(scope.row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" />

      <el-table-column label="操作" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" size="mini" title="修改" @click="edit(scope.row.id)" />
          <el-button type="success" icon="el-icon-s-custom" size="mini" title="分配角色" @click="showRoleAssign(scope.row)" />
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

    <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%">
      <el-form ref="dataForm" :model="sysUser" label-width="100px" size="small" style="padding-right: 40px;">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="sysUser.username" />
        </el-form-item>
        <el-form-item v-if="!sysUser.id" label="密码" prop="password">
          <el-input v-model="sysUser.password" type="password" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="sysUser.name" />
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input v-model="sysUser.phone" />
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="sysUser.deptId" clearable style="width: 100%">
            <el-option v-for="item in deptOptions" :key="item.id" :label="item.label" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="岗位">
          <el-select v-model="sysUser.postId" clearable style="width: 100%">
            <el-option v-for="item in postOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="sysUser.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="sysUser.description" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" icon="el-icon-refresh-right" @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" icon="el-icon-check" size="small" @click="saveOrUpdate()">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="分配角色" :visible.sync="roleDialogVisible" width="460px">
      <el-checkbox-group v-model="checkedRoleIds">
        <el-checkbox v-for="role in roleOptions" :key="role.id" :label="role.id">
          {{ role.roleName }}
        </el-checkbox>
      </el-checkbox-group>
      <span slot="footer">
        <el-button size="small" @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" size="small" @click="submitRoles">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import api from '@/api/system/sysUser'
import sysDeptApi from '@/api/system/sysDept'
import sysPostApi from '@/api/system/sysPost'
import sysRoleApi from '@/api/system/sysRole'
const defaultForm = {
  id: '',
  username: '',
  password: '',
  name: '',
  phone: '',
  status: 1
}
export default {
  data() {
    return {
      listLoading: true, // 数据是否正在加载
      list: null, // banner列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数
      searchObj: {}, // 查询表单对象

      createTimes: [],

      dialogVisible: false,
      sysUser: defaultForm,
      saveBtnDisabled: false,
      deptOptions: [],
      postOptions: [],
      roleOptions: [],
      roleDialogVisible: false,
      currentUserId: null,
      checkedRoleIds: []
    }
  },

  // 生命周期函数：内存准备完毕，页面尚未渲染
  created() {
    this.fetchData()
    this.loadOptions()
  },

  methods: {
    // 加载banner列表数据
    fetchData(page = 1) {
      this.page = page
      if (this.createTimes && this.createTimes.length === 2) {
        this.searchObj.createTimeBegin = this.createTimes[0]
        this.searchObj.createTimeEnd = this.createTimes[1]
      }

      api.getPageList(this.page, this.limit, this.searchObj).then(
        response => {
          // this.list = response.data.list
          this.list = response.data.records
          this.total = response.data.total

          // 数据加载并绑定成功
          this.listLoading = false
        }
      )
    },

    // 重置查询表单
    resetData() {
      this.searchObj = {}
      this.createTimes = []
      this.fetchData()
    },

    // 根据id删除数据
    removeDataById(id) {
      // debugger
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => { // promise
        // 点击确定，远程调用ajax
        return api.removeById(id)
      }).then((response) => {
        this.fetchData(this.page)
        this.$message.success(response.message || '删除成功')
      }).catch(() => {
        this.$message.info('取消删除')
      })
    },

    // 弹出添加表单
    add() {
      this.dialogVisible = true
      this.sysUser = Object.assign({}, defaultForm)
    },
    // 编辑
    edit(id) {
      this.dialogVisible = true
      api.getById(id).then(response => {
        this.sysUser = response.data
      })
    },

    // 添加或更新
    saveOrUpdate() {
      this.saveBtnDisabled = true // 防止表单重复提交
      this.dialogVisible = false // 隐藏表单
      if (!this.sysUser.id) {
        this.save()
      } else {
        this.update()
      }
    },

    // 添加
    save() {
      api.save(this.sysUser).then(response => {
        this.$message.success('操作成功')
        this.fetchData(this.page)
      })
    },

    // 更新
    update() {
      api.updateById(this.sysUser).then(response => {
        this.$message.success(response.message || '操作成功')
        this.fetchData(this.page)
      })
    },

    loadOptions() {
      sysDeptApi.findNodes().then(response => {
        this.deptOptions = this.flattenTree(response.data || [])
      })
      sysPostApi.findAll().then(response => {
        this.postOptions = response.data || []
      })
      sysRoleApi.findAll().then(response => {
        this.roleOptions = response.data || []
      })
    },

    flattenTree(nodes, level = 0) {
      const result = []
      const children = nodes || []
      children.forEach(node => {
        result.push({
          id: node.id,
          label: `${'-- '.repeat(level)}${node.name}`
        })
        result.push(...this.flattenTree(node.children, level + 1))
      })
      return result
    },

    getDeptName(id) {
      const dept = this.deptOptions.find(item => item.id === id)
      return dept ? dept.label.replace(/^(-- )+/, '') : ''
    },

    getPostName(id) {
      const post = this.postOptions.find(item => item.id === id)
      return post ? post.name : ''
    },

    switchStatus(row) {
      api.updateStatus(row.id, row.status).then(() => {
        this.$message.success('状态更新成功')
      }).catch(() => {
        this.fetchData(this.page)
      })
    },

    showRoleAssign(row) {
      this.currentUserId = row.id
      sysRoleApi.getRolesByUserId(row.id).then(response => {
        this.checkedRoleIds = (response.data.userRolesIds || []).map(item => Number(item))
        this.roleDialogVisible = true
      })
    },

    submitRoles() {
      sysRoleApi.doAssignRole({
        userId: this.currentUserId,
        roleIdList: this.checkedRoleIds
      }).then(() => {
        this.roleDialogVisible = false
        this.$message.success('角色分配成功')
      })
    }
  }
}
</script>
