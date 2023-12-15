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
    transformRequest: [function (data) {
      let ret = '';
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    data
  })
}

export function publishPost(data) {
  return request({
    url: '/publishPost',
    method: 'post',
    transformRequest: [function (data) {
      let ret = '';
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    data
  })
}

export function publishComment(data) {
  return request({
    url: '/publishComment',
    method: 'post',
    transformRequest: [function (data) {
      let ret = '';
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    data
  })
}

export function resetPassword(data) {
  return request({
    url: '/resetPassword',
    method: 'post',
    transformRequest: [function (data) {
      let ret = '';
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    data
  })
}

export function sendResetVerifyCode(email) {
  return request({
    url: '/resetCheckCode',
    method: 'get',
    params: {
      email: email,
    }
  })
}

export function sendRegisterVerifyCode(email) {
  return request({
    url: '/signupCheckCode',
    method: 'get',
    params: {
      email: email,
    }
  })
}

export function register(data) {
  return request({
    url: '/signup',
    method: 'post',
    transformRequest: [function (data) {
      let ret = '';
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    data
  })
}

export function createClass(data) {
  return request({
    url: '/createClass',
    method: 'post',
    transformRequest: [function (data) {
      let ret = '';
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    data
  })
}

export function getAccessCode(classid, token, userid) {
  return request({
    url: '/getAccessCode',
    method: 'get',
    params: {
      classid: parseInt(classid),
      token: token,
      userid: parseInt(userid)
    }
  })
}

export function getDuplicateReport(token, filetoken) {
  return request({
    url: '/getContentCheck',
    method: 'get',
    params: {
      token: token,
      checkToken: filetoken
    }
  })
}

export function joinClass(data) {
  return request({
    url: '/attendCourse',
    method: 'post',
    transformRequest: [function (data) {
      let ret = '';
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    data
  })
}

export function assignHomework(data) {
  let formData = new FormData();
  const emptyFile = new File([], 'empty.txt');
  formData.append('attachment', emptyFile);
  for (let key in data) {
    formData.append(key, data[key]);
  }
  return request({
    url: '/addHomework',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data; boundary=--------------------------447331697369778982143514'
    },

    data: formData
  })
}
export function getAllTeachingClasses(token, userid) {
  return request({
    url: '/selectAllTeachingClasses',
    method: 'get',
    params: {
      token: token,
      userid: userid
    }
  })
}

export function getAllListeningClasses(token, userid) {
  return request({
    url: '/getAttendedCourse',
    method: 'get',
    params: {
      token: token,
      userid: userid
    }
  })
}

export function getStuentList(userid, token, id) {
  return request({
    url: '/getAttendeeList',
    method: 'get',
    params: {
      classid: id,
      userid: userid,
      token: token
    }
  })
}
export function getHomeworkList(userid, token, classid) {
  return request({
    url: '/getHomeworkList',
    method: 'get',
    params: {
      classid: classid,
      userid: userid,
      token: token
    }
  })
}

export function getPostList(userid, token, classid) {
  return request({
    url: '/getPostList',
    method: 'get',
    params: {
      classid: classid,
      userid: userid,
      token: token,
    }
  })
}

export function getPostListAndComments(userid, token, classid, postid) {
  return request({
    url: '/getPostDetailsAndComments',
    method: 'get',
    params: {
      userid: userid,
      token: token,
      classid: classid,
      postid: postid,
    }
  })
}

export function getHomeworkDetail(userid, token, classid, homeworkid) {
  return request({
    url: '/getHomeworkDetails',
    method: 'get',
    params: {
      classid: classid,
      userid: userid,
      token: token,
      homeworkid: homeworkid,
    }
  })
}


export function myGetInfo(token, userid) {
  return request({
    url: '/getInformation',
    method: 'get',
    params: {
      token: token,
      userid: userid
    }
  })
}

export function getInfo(token) {
  return request({
    url: '/vue-element-admin/user/info',
    method: 'get',
    baseURL: '/dev-api',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/vue-element-admin/user/logout',
    method: 'post'
  })
}
