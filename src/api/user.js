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

export function addCommit(formData) {
  let data = new FormData();
  for (let key in formData) {
    data.append(key, formData[key]);
  }
  return request({
    url: '/addSubmit',
    method: 'post',
    headers: {
      'Access-Control-Allow-Origin': '*',
      'Content-Type': 'multipart/form-data; boundary=--------------------------447331697369778982143514'
    },
    data: data
  })
}

export function addResource(formData) {
  let data = new FormData();
  for (let key in formData) {
    data.append(key, formData[key]);
  }
  return request({
    url: '/addClassSource',
    method: 'post',
    headers: {
      'Access-Control-Allow-Origin': '*',
      'Content-Type': 'multipart/form-data; boundary=--------------------------447331697369778982143514'
    },
    data: data
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

export function postReassessment(data) {
  return request({
    url: '/postReassessment',
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

export function correctHomework(data) {
  return request({
    url: '/correctHomework',
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

export function recorrectHomework(data) {
  return request({
    url: '/reassessSubmit',
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


export function modifyPassword(data) {
  return request({
    url: '/modifyPassword',
    method: 'put',
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

export function modifyName(data) {
  return request({
    url: '/modifyName',
    method: 'put',
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

export function getInformation(token, userid) {
  return request({
    url: '/getInformation',
    method: 'get',
    params: {
      token: token,
      userid: userid,
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

export function setAllocate(data) {
  return request({
    url: '/allocateHomework',
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

export function getSubmitList(homeworkid, token, userid) {
  return request({
    url: '/getSubmitList',
    method: 'get',
    params: {
      homeworkid: parseInt(homeworkid),
      token: token,
      userid: parseInt(userid)
    }
  })
}
export function getReassessSubmitList(token, userid) {
  return request({
    url: '/getReassessmentList',
    method: 'get',
    params: {
      token: token,
      userid: parseInt(userid)
    }
  })
}


export function getSubmitListStudent(homeworkid, token, userid) {
  return request({
    url: '/getSubmitListStudent',
    method: 'get',
    params: {
      homeworkid: parseInt(homeworkid),
      token: token,
      userid: parseInt(userid)
    }
  })
}

export function getHomeworkStatisticStage(homeworkid, token, userid) {
  return request({
    url: '/getHomeworkStatisticStage',
    method: 'get',
    params: {
      homeworkid: parseInt(homeworkid),
      token: token,
      userid: parseInt(userid)
    }
  })
}
export function getSourceList(classid, token, userid) {
  return request({
    url: '/getResourceList',
    method: 'get',
    params: {
      classid: parseInt(classid),
      token: token,
      userid: parseInt(userid)
    }
  })
}


export function getEvaluateList(token, userid) {
  return request({
    url: '/getAllocatedList',
    method: 'get',
    params: {
      token: token,
      userid: parseInt(userid)
    }
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
