/*
角色管理相关的API请求函数
*/
import request from '@/utils/request'

const api_name = '/admin/system/sysRole'

export default {

  /*
  获取角色分页列表(带搜索)
  */
  findPage(pageNum, pageSize, searchObj) {
    return request({
      url: `${api_name}/${pageNum}/${pageSize}`,
      method: 'get',
      params: searchObj
    })
  },

  removeById(sysRoleId) {
    return request({
      url: `${api_name}/remove/${sysRoleId}`,
      method: 'delete'
    })
  },
  save(sysRole) {
    return request({
      url: `${api_name}/add`,
      method: 'post',
      data: sysRole
    })
  },
  getRoleId(sysRoleId) {
    return request({
      url: `${api_name}/get/${sysRoleId}`,
      method: 'get'
    })
  },
  update(sysRole) {
    return request({
      url: `${api_name}/update`,
      method: 'post',
      data: sysRole
    })
  },
  batchRemove(idList) {
    return request({
      url: `${api_name}/batchRemove`,
      method: 'delete',
      data: idList
    })
  },

  findAll() {
    return request({
      url: `${api_name}/findAll`,
      method: 'get'
    })
  },

  getRolesByUserId(userId) {
    return request({
      url: `${api_name}/getRolesByUserId/${userId}`,
      method: 'get'
    })
  },

  doAssignRole(data) {
    return request({
      url: `${api_name}/doAssignRole`,
      method: 'post',
      data
    })
  }
}
