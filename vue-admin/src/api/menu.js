import request from '@/utils/request'

// 获取路由
export const getRouters = () => {
  return request({
    //url: '/dev/auth/routers?menu=dev_menus',
    url: '/admin-api/menus.json?x=' + new Date().getTime(),
    method: 'get'
  })
}


