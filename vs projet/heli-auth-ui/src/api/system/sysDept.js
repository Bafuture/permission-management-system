import request from '@/utils/request'

const api_name = '/admin/system/sysDept'

export default {
  findNodes() {
    return request({
      url: `${api_name}/findNodes`,
      method: 'get'
    })
  },

  getById(id) {
    return request({
      url: `${api_name}/get/${id}`,
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

  updateStatus(id, status) {
    return request({
      url: `${api_name}/updateStatus/${id}/${status}`,
      method: 'get'
    })
  }
}
