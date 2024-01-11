import request from '@/utils/request'
// 根据字典类型查询字典数据信息
export function getDicts(dictType) {
    return request({
      url: '/admin/dict/get_by_type?dictType=' + dictType,
      method: 'get'
    })
  }