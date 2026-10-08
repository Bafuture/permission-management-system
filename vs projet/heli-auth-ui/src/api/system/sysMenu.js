import request from '@/utils/request'

const api_name = '/admin/system/sysMenu'

export default {
  findNodes() {
    return request({
      url: `${api_name}/findNodes`,
      method: 'get'
    })
  },

  getById(id) {
    return request({
      url: `${api_name}/getById/${id}`,
      method: 'get'
    })
  },

  save(data) {
    return request({
      url: `${api_name}/save`,
      method: 'post',
      data
    })
  },

  update(data) {
    return request({
      url: `${api_name}/update`,
      method: 'put',
      data
    })
  },

  removeById(id) {
    return request({
      url: `${api_name}/remove/${id}`,
      method: 'delete'
    })
  },

  toAssign(roleId) {
    return request({
      url: `${api_name}/toAssign/${roleId}`,
      method: 'get'
    })
  },

  doAssign(data) {
    return request({
      url: `${api_name}/doAssign`,
      method: 'post',
      data
    })
  },

  getRoleMenuList(roleId) {
    return request({
      url: `${api_name}/getRoleMenuList/${roleId}`,
      method: 'get'
    })
  }
}
