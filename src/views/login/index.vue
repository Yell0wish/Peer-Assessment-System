<template>
<!--    <div class="login-container">-->
<!--        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" autocomplete="on" label-position="left">-->

<!--            <div class="title-container">-->
<!--                <h3 class="title">-->
<!--                    {{ $t('login.title') }}-->
<!--                </h3>-->
<!--                &lt;!&ndash;        <lang-select class="set-language" />&ndash;&gt;-->
<!--            </div>-->

<!--            <el-form-item prop="username">-->
<!--        <span class="svg-container">-->
<!--          <svg-icon icon-class="user" />-->
<!--        </span>-->
<!--                <el-input-->
<!--                        ref="username"-->
<!--                        v-model="loginForm.username"-->
<!--                        :placeholder="$t('login.username')"-->
<!--                        name="username"-->
<!--                        type="text"-->
<!--                        tabindex="1"-->
<!--                        autocomplete="on"-->
<!--                />-->
<!--            </el-form-item>-->

<!--            <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" manual>-->
<!--                <el-form-item prop="password">-->
<!--          <span class="svg-container">-->
<!--            <svg-icon icon-class="password" />-->
<!--          </span>-->
<!--                    <el-input-->
<!--                            :key="passwordType"-->
<!--                            ref="password"-->
<!--                            v-model="loginForm.password"-->
<!--                            :type="passwordType"-->
<!--                            :placeholder="$t('login.password')"-->
<!--                            name="password"-->
<!--                            tabindex="2"-->
<!--                            autocomplete="on"-->
<!--                            @keyup.native="checkCapslock"-->
<!--                            @blur="capsTooltip = false"-->
<!--                            @keyup.enter.native="handleLogin"-->
<!--                    />-->
<!--                    <span class="show-pwd" @click="showPwd">-->
<!--            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />-->
<!--          </span>-->
<!--                </el-form-item>-->
<!--            </el-tooltip>-->
<!--            <div style="float: left">-->
<!--                <el-button size="medium" :loading="loading" type="primary" @click.native.prevent="handleLogin">-->
<!--                    {{ $t('login.logIn') }}-->
<!--                </el-button>-->
<!--            </div>-->
<!--            <div style="float: right;">-->
<!--                <el-button size="medium" :loading="loading" type="primary" @click.native.prevent="handleRegister">-->
<!--                    注册-->
<!--                </el-button>-->
<!--            </div>-->
<!--            <div style="margin-right: 5%; float: right;">-->
<!--                <el-button size="medium" :loading="loading" type="primary" @click.native.prevent='handleModify'>-->
<!--                    忘记密码-->
<!--                </el-button>-->
<!--            </div>-->



<!--            &lt;!&ndash;      <div style="position:relative">&ndash;&gt;-->
<!--            &lt;!&ndash;        <div class="tips">&ndash;&gt;-->
<!--            &lt;!&ndash;          <span>{{ $t('login.username') }} : admin</span>&ndash;&gt;-->
<!--            &lt;!&ndash;          <span>{{ $t('login.password') }} : {{ $t('login.any') }}</span>&ndash;&gt;-->
<!--            &lt;!&ndash;        </div>&ndash;&gt;-->
<!--            &lt;!&ndash;        <div class="tips">&ndash;&gt;-->
<!--            &lt;!&ndash;          <span style="margin-right:18px;">&ndash;&gt;-->
<!--            &lt;!&ndash;            {{ $t('login.username') }} : editor&ndash;&gt;-->
<!--            &lt;!&ndash;          </span>&ndash;&gt;-->
<!--            &lt;!&ndash;          <span>{{ $t('login.password') }} : {{ $t('login.any') }}</span>&ndash;&gt;-->
<!--            &lt;!&ndash;        </div>&ndash;&gt;-->

<!--            &lt;!&ndash;        <el-button class="thirdparty-button" type="primary" @click="showDialog=true">&ndash;&gt;-->
<!--            &lt;!&ndash;          {{ $t('login.thirdparty') }}&ndash;&gt;-->
<!--            &lt;!&ndash;        </el-button>&ndash;&gt;-->
<!--            &lt;!&ndash;      </div>&ndash;&gt;-->
<!--        </el-form>-->

<!--        <el-dialog :title="$t('login.thirdparty')" :visible.sync="showDialog">-->
<!--            {{ $t('login.thirdpartyTips') }}-->
<!--            <br>-->
<!--            <br>-->
<!--            <br>-->
<!--            <social-sign />-->
<!--        </el-dialog>-->
<!--    </div>-->
    <div class="page-box">
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" label-width="100px" class="register" autocomplete="on" label-position="left" >
            <h1 style="text-align: center">登录系统</h1>
            <el-form-item label="邮箱：" prop="username">
                <el-input
                        ref="username"
                        v-model="loginForm.username"
                        placeholder="请输入邮箱"
                        name="username"
                        type="text"
                        tabindex="1"
                        autocomplete="on"
                />
            </el-form-item>

            <el-form-item label="密码：" prop="password">
                <el-input
                        :key="passwordType"
                        ref="password"
                        v-model="loginForm.password"
                        placeholder="请输入密码"
                        :type="passwordType"
                        name="password"
                        tabindex="2"
                        autocomplete="on"
                        @keyup.native="checkCapslock"
                        @blur="capsTooltip = false"
                        @keyup.enter.native="handleLogin"
                        show-password clearable
                />
            </el-form-item>

            <el-form-item label-width="0px">
                <div style="float: left">
                    <el-button size="medium" :loading="loading" type="primary" @click.native.prevent="handleRegister">
                        注册
                    </el-button>
                </div>
                <div style="float: right;">
                    <el-button size="medium" :loading="loading" type="primary" @click.native.prevent="handleLogin">
                        {{ $t('login.logIn') }}
                    </el-button>
                </div>
                <div style="margin-right: 5%; float: right;">
                    <el-button size="medium" :loading="loading" type="primary" @click.native.prevent='handleModify'>
                        忘记密码
                    </el-button>
                </div>
            </el-form-item>

        </el-form>
    </div>
</template>

<script>
import {validEmail, validUsername} from '@/utils/validate'
import LangSelect from '@/components/LangSelect'
import SocialSign from './components/SocialSignin'

export default {
  name: 'Login',
  components: { LangSelect, SocialSign },
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validEmail(value)) {
        callback(new Error('请输入正确的用户名'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码不能少于6位'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginForm1: {
        username: 'editor',
        password: '111111'
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      passwordType: 'password',
      capsTooltip: false,
      loading: false,
      showDialog: false,
      redirect: undefined,
      otherQuery: {}
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        const query = route.query
        if (query) {
          this.redirect = query.redirect
          this.otherQuery = this.getOtherQuery(query)
        }
      },
      immediate: true
    }
  },
  created() {
    // window.addEventListener('storage', this.afterQRScan)
  },
  mounted() {
    if (this.loginForm.username === '') {
      this.$refs.username.focus()
    } else if (this.loginForm.password === '') {
      this.$refs.password.focus()
    }
  },
  destroyed() {
    // window.removeEventListener('storage', this.afterQRScan)
  },
  methods: {
    checkCapslock(e) {
      const { key } = e
      this.capsTooltip = key && key.length === 1 && (key >= 'A' && key <= 'Z')
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleModify() {
      this.$router.push('/modify-password')
    },
    handleRegister() {
      this.$router.push("/register")
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/mylogin', this.loginForm)
              .then(() => {
                this.$router.push({ path: this.redirect || '/', query: this.otherQuery });
                this.loading = false;
              })
              .catch(error => {
                console.log(error);
                this.loading = false;
              });
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    getOtherQuery(query) {
      return Object.keys(query).reduce((acc, cur) => {
        if (cur !== 'redirect') {
          acc[cur] = query[cur]
        }
        return acc
      }, {})
    }
    // afterQRScan() {
    //   if (e.key === 'x-admin-oauth-code') {
    //     const code = getQueryObject(e.newValue)
    //     const codeMap = {
    //       wechat: 'code',
    //       tencent: 'code'
    //     }
    //     const type = codeMap[this.auth_type]
    //     const codeName = code[type]
    //     if (codeName) {
    //       this.$store.dispatch('LoginByThirdparty', codeName).then(() => {
    //         this.$router.push({ path: this.redirect || '/' })
    //       })
    //     } else {
    //       alert('第三方登录失败')
    //     }
    //   }
    // }
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
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

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

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: white;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: black;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }

    .set-language {
      color: #fff;
      position: absolute;
      top: 3px;
      font-size: 18px;
      right: 0px;
      cursor: pointer;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    cursor: pointer;
    user-select: none;
  }

  .thirdparty-button {
    position: absolute;
    right: 0;
    bottom: 6px;
  }

  @media only screen and (max-width: 470px) {
    .thirdparty-button {
      display: none;
    }
  }
}

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
