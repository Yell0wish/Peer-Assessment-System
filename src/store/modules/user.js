import {
  login,
  logout,
  getInfo,
  mylogin,
  sendResetVerifyCode,
  resetPassword,
  sendRegisterVerifyCode,
  register,
  myGetInfo,
  createClass,
  joinClass,
  getAccessCode,
  getAllTeachingClasses,
  getAllListeningClasses,
  getStuentList, getHomeworkList
} from '@/api/user'
import {getToken, setToken, removeToken, getUserid, setUserid} from '@/utils/auth'
import router, { resetRouter } from '@/router'
import role from "@/views/permission/role.vue";

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  introduction: '',
  roles: [],
  userid: getUserid(),
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_USERID: (state, userid) => {
    state.userid = userid
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user login by Zoransy
  mylogin({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      mylogin({ email: username, password: password }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        commit('SET_USERID', data.userid)
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  sendResetVerifyCode({ commit }, email) {
    return new Promise((resolve, reject) => {
      sendResetVerifyCode(email).then(response => {
        const { data } = response
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  sendRegisterVerifyCode({ commit }, email) {
    return new Promise((resolve, reject) => {
      sendRegisterVerifyCode(email).then(response => {
        const { data } = response
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  createClass({ commit }, classname){
    return new Promise((resolve, reject) => {
      createClass({userid: state.userid, token: state.token, className: classname}).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  getAccessCode({ commit, state}, classID) {
    return new Promise((resolve, reject) => {
      getAccessCode(classID, state.token, state.userid).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  joinClass({ commit }, accesscode){
    return new Promise((resolve, reject) => {
      joinClass({userid: state.userid, token: state.token, accessCode: accesscode}).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // reset user password by Zoransy
  resetPassword({ commit }, passwordInfo) {
    const { email, newPassword, checkcode } = passwordInfo;
    return new Promise((resolve, reject) => {
      resetPassword({email, newPassword, checkcode: checkcode.trim()}).then(response => {
        const { data } = response;
        resolve();
      }).catch(error => {
        reject(error);
      });
    });
  },

  register({ commit }, userInfo) {
    const { email, passwd, passwdagain, verifyCode} = userInfo;
    return new Promise((resolve, reject) => {
      register({email: email, password: passwd, checkcode: verifyCode.trim()}).then(response => {
        const { data } = response;
        resolve();
      }).catch(error => {
        reject(error);
      });
    });
  },

  myGetInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      myGetInfo(state.token, state.userid).then(response => {
        const { data } = response
        const fixedData = {
          roles: ['admin'],
          introduction : '我是自我介绍',
          avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
          username: 'super admin'
        }
        if (!data) {
          reject('Verification failed, please Login again.')
        }

        commit('SET_ROLES', fixedData.roles)
        commit('SET_NAME', data.userInformation.username)
        commit('SET_USERID', data.userInformation.uuid)
        commit('SET_AVATAR', fixedData.avatar)
        commit('SET_INTRODUCTION', fixedData.introduction)
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        setUserid(data.userInformation.uuid)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  getTeachingCourses({ commit, state }) {
    return new Promise((resolve, reject) => {
      getAllTeachingClasses(state.token, state.userid).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  getListeningCourses({ commit, state }) {
    return new Promise((resolve, reject) => {
      getAllListeningClasses(state.token, state.userid).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  getStudentList({ commit, state }, classid) {
    return new Promise((resolve, reject) => {
      getStuentList(parseInt(state.userid), state.token, parseInt(classid)).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },
  getHomeworkList({ commit, state }, classid) {
    return new Promise((resolve, reject) => {
      getHomeworkList(parseInt(state.userid), state.token, parseInt(classid)).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  getHomeworkDetail({ commit, state }, classid, homeworkid) {
    return new Promise((resolve, reject) => {
      getHomeworkDetail(parseInt(state.userid), state.token, parseInt(classid), parseInt(homeworkid)).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        setToken(data.token)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response
        const fixeddata = {
          roles: ['admin'],
          introduction : '我是自我介绍',
          avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
          name: 'super admin'
        }

        if (!data) {
          reject('Verification failed, please Login again.')
        }


        commit('SET_ROLES', fixeddata.roles)
        commit('SET_NAME', fixeddata.name)
        commit('SET_AVATAR', fixeddata.avatar)
        commit('SET_INTRODUCTION', fixeddata.introduction)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
      // const data = {
      //   roles: ['admin'],
      //   introduction : '我是自我介绍',
      //   avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
      //   name: 'super admin'
      // }
      // if (!data) {
      //   reject('Verification failed, please Login again.')
      // }
      //
      // // roles must be a non-empty array
      // if (!roles || roles.length <= 0) {
      //   reject('getInfo: roles must be a non-null array!')
      // }
      //
      // commit('SET_ROLES', data.roles)
      // commit('SET_NAME', data.username)
      // commit('SET_AVATAR', data.avatar)
      // commit('SET_INTRODUCTION', data.introduction)
      // // commit('SET_TOKEN', data.token)
      // // setToken(data.token)
      // resolve(data)
    })
  },

  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
        // logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        removeToken()
        resetRouter()

        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews', null, { root: true })

        resolve()
      // }).catch(error => {
      //   reject(error)
      // })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // dynamically modify permissions
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    const { roles } = await dispatch('getInfo')

    resetRouter()

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
