import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/ruoyi";

// 查询用户列表
export function listUser(query) {
  return request({
    url: '/admin/user/list',
    method: 'get',
    params: query
  })
}

// 查询用户详细
export function getDetailForEdit(userId) {
  return request({
    url: '/admin/user/detail_for_edit?id=' + praseStrEmpty(userId),
    method: 'get'
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/admin/user/add',
    method: 'post',
    data: data
  })
}

// 修改用户
export function updateUser(data) {
  return request({
    url: '/admin/user/edit',
    method: 'post',
    data: data
  })
}

// 删除用户
export function delUser(ids) {
  return request({
    url: '/admin/user/delete?ids=' + ids,
    method: 'get'
  })
}

// // 导出用户
// export function exportUser(query) {
//   return request({
//     url: '/admin/user/export',
//     method: 'get',
//     params: query
//   })
// }

// 用户密码重置
export function resetUserPwd(id, password) {
  const data = {
    id,
    password
  }
  return request({
    url: '/admin/user/resetPwd',
    method: 'post',
    data: data
  })
}

// 用户状态修改
export function changeUserStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/admin/user/changeStatus',
    method: 'post',
    data: data
  })
}

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/admin/profile/get',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/admin/profile/edit',
    method: 'post',
    data: data
  })
}

// 用户密码重置
export function updateUserPwd(oldPassword, newPassword) {
  const data = {
    oldPassword,
    newPassword
  }
  return request({
    url: '/admin/profile/pwd_update',
    method: 'post',
    data: data
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: '/admin/profile/avatar',
    method: 'post',
    data: data
  })
}

// 下载用户导入模板
export function importTemplate() {
  return request({
    url: '/admin/user/importTemplate',
    method: 'get'
  })
}
