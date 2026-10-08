import request from '@/utils/request'

const api_name = '/admin/system/sysPost'

export default {
  findPage(page, limit, searchObj) {
    return request({
      url: `${api_name}/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },

  findAll() {
    return request({
      url: `${api_name}/findAll`,
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
