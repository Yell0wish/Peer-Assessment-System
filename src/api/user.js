import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/vue-element-admin/user/login',
    method: 'post',
    data
  })
}

export function mylogin(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function resetPassword(data) {
  return request({
    url: '/resetPassword',
    method: 'put',
    data
  })
}

export function register(data) {
  return request({
    url: '/signup',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/vue-element-admin/user/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-element-admin/user/logout',
    method: 'post'
  })
}