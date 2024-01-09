import request from '@/utils/request'

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    //url: '/dev/auth/login_pwd',
   // method: 'post',
    url: '/admin-api/login.json',
    method: 'get',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    //url: '/dev/auth/profile',
    url: '/admin-api/profile.json',
    method: 'get'
  })
}

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/admin-api/profile2.json',
    method: 'get'
  })
}

// 用户密码重置
export function updateUserPwd(oldPassword, newPassword) {
  const data = {
    oldPassword,
    newPassword
  }
  return request({
    url: '/dev/profile/pwd_update',
    method: 'post',
    params: data
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: '/dev/profile/avatar',
    method: 'post',
    data: data
  })
}
// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/dev/profile/edit',
    method: 'post',
    data: data
  })
}


// 退出方法
export function logout() {
  return request({
    //url: '/dev/auth/logout',
    //method: 'post'
    url: '/admin-api/logout.json',
    method: 'get'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/admin-api/catpcha.json',
    method: 'get'
  })
}
