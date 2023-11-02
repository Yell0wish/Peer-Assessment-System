<template>
  <div class="register">

    <h1 style="text-align: center">注册</h1>
    <el-form ref="registerForm" :model="registerForm" :rules="rules" label-width="100px">
      <el-form-item label="邮箱:" prop="email">
        <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>

      <el-form-item label="密码：" prop="passwd">
        <el-input v-model="registerForm.passwd" placeholder="请输入密码" show-password clearable></el-input>
      </el-form-item>

      <el-form-item label="再次密码：" prop="passwdagain">
        <el-input v-model="registerForm.passwdagain" placeholder="请确认密码" show-password clearable></el-input>
      </el-form-item>

      <el-form-item label="验证码:" prop="verifyCode">
        <el-input v-model="registerForm.verifyCode" placeholder=请输入验证码></el-input>
      </el-form-item>

      <el-form-item label-width="0px">
        <el-button size="medium" style="border-color: #ded9d9;" @click="clickSend">发送验证码</el-button>
        <el-button type="primary" size="medium" :loading = "isLoading"  @click="clickRegister">{{text}}</el-button>
      </el-form-item>

    </el-form>
  </div>
</template>

<script>
export default {
  name: "register",
  data() {
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.registerForm.passwd) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };

    return {
      state: -1,
      text: "创建账号",
      isDisabled: false,
      isLoading: false,
      registerForm: {
        email:'',
        passwd: '',
        passwdagain: '',
        verifyCode: ''
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
        passwdagain: [
          {
            required: true, message: "请输入密码", trigger: 'blur'
          },
          {
            min: 6, message: "密码必须大于6", trigger: 'blur'
          },
          {
            validator: validatePass2, trigger: 'blur'
          }
        ]
      },
    }
  },

  methods: {
    clickRegister() {
      if (this.isLoading) {
        this.text = ""
      }
      //获取到的是添加了ref="registerForm"属性的这个组件 前端判断
      this.$refs["registerForm"].validate((valid) => {
        if(this.registerForm.verifyCode === ''){
          alert('验证码不能为空');
          return ;
        }
        if(valid && this.registerForm.verifyCode !== '') {
          this.$axios({
            url: this.$url + "/signup",
            method: 'post',
            data: {
              email: this.registerForm.email,//邮箱
              password : this.registerForm.passwd,
              checkcode: this.registerForm.verifyCode,
            },
            transformRequest: [function (data) {
              let ret = '';
              for (let it in data) {
                ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
              }
              return ret
            }],
          }).then(res => {
              window.console.log(res.data.message);
              if(res.data.code === 200){
                this.$axios({//向指定资源提交数据
                  url: this.$url + "/login",//请求路径

                  method: 'post',
                  data: {//提交id 密码
                    email:this.registerForm.email,
                    //passwd: this.$md5(this.form.passwd + this.$salt),
                    password: this.registerForm.passwd,
                  },

                  transformRequest: [function (data) {
                    let ret = '';
                    for (let it in data) {
                      ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                    }
                    return ret
                  }],
                }).then(res => {
                  this.$store.dispatch('user/login', this.loginForm)
                  this.$router.push({ path: this.redirect || '/', query: this.otherQuery })
                })
              }
          })
          // console.log('success')
        }else  {
          this.text = "创建账户";
          this.isLoading = false;
          this.$alert('账号不成立！', '警告', {
            confirmButtonText: '确定',
            callback: {
            }
          });
          // console.log('error submit!!');
          return false;
        }
      })
    },

    loginProcess() {
      if (this.state === 0) {
        this.$router.push('/mainFrame')
      } else {
        this.text = "登录";
        this.isLoading = false;
      }
    },
    clickSend() {
    if (this.registerForm.email !== '') {
      this.$axios({
        url: 'http://192.168.31.13:8080/signupCheckCode', // 请求路径
        method: 'get',
        params: {
          email: this.registerForm.email
        }
      }).then(res => {
        window.console.log("received" + res.data.message);
      });
    }
    else{
      alert('邮箱不能为空');
    }

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
