import request from '@/utils/request'

const api_name = '/admin/system/sysOperLog'

export default {
  findPage(page, limit, searchObj) {
    return request({
      url: `${api_name}/${page}/${limit}`,
      method: 'get',
      params: searchObj
    })
  },

  getById(id) {
    return request({
      url: `${api_name}/get/${id}`,
      method: 'get'
    })
  }
}
