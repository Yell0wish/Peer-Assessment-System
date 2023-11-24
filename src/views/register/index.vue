<template>
  <div class="page-box">


    <el-form ref="registerForm" :model="registerForm" :rules="rules" label-width="100px" class="register">
        <h1 style="text-align: center">注册账号</h1>
      <el-form-item label="邮箱：" prop="email">
        <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>

      <el-form-item label="密码：" prop="passwd">
        <el-input v-model="registerForm.passwd" placeholder="请输入密码" show-password clearable></el-input>
      </el-form-item>

      <el-form-item label="确认密码：" prop="passwdagain">
        <el-input v-model="registerForm.passwdagain" placeholder="请确认密码" show-password clearable></el-input>
      </el-form-item>

      <el-form-item label="验证码：" prop="verifyCode">
        <el-input v-model="registerForm.verifyCode" placeholder=请输入验证码></el-input>
      </el-form-item>

      <el-form-item label-width="0px">
        <div style="float: left;">
          <el-button type="primary" size="medium" @click="clickSend" :loading = "isLoading">发送验证码</el-button>
        </div>
        <div style="float: right;">
          <el-button type="primary" size="medium" :loading = "isLoading"  @click="clickRegister">{{text}}</el-button>
        </div>
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
      if (!this.registerForm.verifyCode) {
          this.$message({
              type: 'warning',
              message: '验证码不能为空'
          })
          this.isLoading = false
          return
      }
      this.$store.dispatch('user/register', this.registerForm).then(() => {
          this.isLoading = false
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
        if (!this.registerForm.email) {
            this.$message({
                type: 'warning',
                message: '邮箱不能为空'
            })
            this.isLoading = false
            return
        }
        this.$store.dispatch('user/sendRegisterVerifyCode', this.registerForm.email).then(() => {
            this.isLoading = false
        })
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      height: 47px;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
  }
}
</style>

<style lang="scss" scoped>
.page-box{

  background-color: #2d3a4b;
  min-height: 100%;
  width: 100%;
  overflow: hidden;

  .register {
    margin: auto;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -80%);
    width: 25rem;
    position: absolute;
    background-color: white;
    padding: 20px 20px 10px 20px;
    border-radius: 10px;
    box-shadow: 0px 15px 25px 0px rgba(0, 0, 0, 0.11);
  }
}
</style>

