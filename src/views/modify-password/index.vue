<template>
  <div class="register">

    <h1 style="text-align: center">重置密码</h1>
    <el-form ref="modifyForm" :model="modifyForm" :rules="rules" label-width="100px">

      <el-form-item label="邮箱:" prop="email">
        <el-input v-model="modifyForm.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>


      <el-form-item label="新密码：" prop="passwd">
        <el-input v-model="modifyForm.passwd" placeholder="请输入密码" show-password clearable></el-input>
      </el-form-item>

      <el-form-item label="验证码" prop="verifyCode">
        <el-input v-model="modifyForm.verifyCode" placeholder="请输入验证码"></el-input>
      </el-form-item>

      <el-form-item label-width="0px">
        <el-button size="medium" style="border-color: #ded9d9;" @click="clickSend">发送验证码</el-button>
        <el-button type="primary" size="medium" :loading = "isLoading"  @click="clickReset">重置密码</el-button>
      </el-form-item>

    </el-form>
  </div>
</template>

<script>
export default {
  name: "modify",
  data() {

    return {
      state: -1,
      text: "创建账号",
      isDisabled: false,
      isLoading: false,
      modifyForm: {
        email:'',
        passwd: '',
        verifyCode: '',
      },
      rules: {
        email: [//如果为空就显示message
          {
            required: true, message: "请输入邮箱", trigger: 'blur'
          },
        ],
        passwd: [
          {
            required: true, message: "请输入密码", trigger: 'blur'
          },
          {
            min: 6, message: "密码必须大于6", trigger: 'blur'
          }
        ],
      },
    }
  },

  methods: {

    clickReset(){
      this.$refs["modifyForm"].validate((valid) => {
        if(this.modifyForm.verifyCode === ''){
          alert('验证码不能为空');
          return;
        }
        if(valid && this.modifyForm.verifyCode !== '') {
          //alert("valid"+this.$registerUrl);//http://localhost:10087/user/register
          this.$axios({
            url: this.$url + "/resetPassword" ,
            method: 'put',
            data: {
              email: this.modifyForm.email,
              newPassword: this.modifyForm.passwd,
              checkcode: this.modifyForm.verifyCode,
            },
            transformRequest: [function (data) {
              let ret = '';
              for (let it in data) {
                ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
              }
              return ret
            }],
          }).then(res => {
            window.console.log("reset:"+ res.data.message)
          })
          // console.log('success')
        }
      })
    },

    clickSend() {
      window.console.log(this.modifyForm.email)
      this.$axios({
        url: this.$url + "/resetCheckCode" ,
        method: 'get',
        params: {
          email: this.modifyForm.email,
        },
        transformRequest: [function (data) {
          let ret = '';
          for (let it in data) {
            ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
          }
          return ret
        }],
      }).then(res => {
        window.console.log("reset:"+ res.data.message)
      })
        // console.log('success')

    },

    loginProcess() {
      if (this.state === 0) {
        this.$router.push('/mainFrame')
      } else {
        this.text = "登录";
        this.isLoading = false;
      }
    },
    clickLogin() {
      this.$router.push("/login")
    }
  }
}
</script>

<style scoped>

.register {
  margin: auto;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 25rem;
  position: absolute;
  background-color: white;
  padding: 20px 20px 10px 20px;
  border-radius: 10px;
  box-shadow: 0px 15px 25px 0px rgba(0, 0, 0, 0.11);
}

</style>
