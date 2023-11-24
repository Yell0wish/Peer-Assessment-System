import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'
const id = "10007"
export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function getUserid() {
  return Cookies.get(id)
}

export function setUserid(newid) {
  return Cookies.set(id, newid)
}

