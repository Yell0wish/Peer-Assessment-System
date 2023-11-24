<template>
  <div class="page-box">
    <el-form ref="modifyForm" :model="modifyForm" :rules="rules" label-width="100px" class="register">
      <h1 style="text-align: center">重置密码</h1>
      <el-form-item label="邮箱：" prop="email">
        <el-input v-model="modifyForm.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>

      <el-form-item label="新密码：" prop="passwd">
        <el-input v-model="modifyForm.passwd" placeholder="请输入新密码" show-password clearable></el-input>
      </el-form-item>

      <el-form-item label="验证码：" prop="checkcode">
        <el-input v-model="modifyForm.checkcode" placeholder="请输入验证码"></el-input>
      </el-form-item>

      <el-form-item label-width="0px">
        <div style="float: left;">
          <el-button type="primary" size="medium" @click="clickSend" :loading = "isLoading">发送验证码</el-button>
        </div>
        <div style="float: right;">
          <el-button type="primary" size="medium" :loading = "isLoading"  @click="clickReset">重置密码</el-button>
        </div>
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
      isDisabled: false,
      isLoading: false,
      modifyForm: {
        email:'',
        passwd: '',
        checkcode: '',
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
      if (!this.modifyForm.email) {
        this.$message({
          type: 'warning',
          message: '邮箱不能为空'
        })
        this.isLoading = false
        return
      }
      this.$store.dispatch('user/resetPassword', this.modifyForm)
        .then(() => {
          this.isLoading = false
        })
    },

    clickSend() {
      if (!this.modifyForm.checkcode) {
        this.$message({
          type: 'warning',
          message: '验证码不能为空'
        })
        this.isLoading = false
        return
      }
      this.$store.dispatch('user/sendResetVerifyCode', this.modifyForm.email)
        .then(() => {
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
    clickLogin() {
      this.$router.push("/login")
    }
  }
}
</script>

<style scoped>

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
