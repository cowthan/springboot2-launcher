import request from '@/utils/request'

// 获取路由
export const getRouters = () => {
  return request({
    url: '/admin/auth/routers',
    //url: '/admin-api/menus.json?x=' + new Date().getTime(),
    method: 'get'
  })
}


