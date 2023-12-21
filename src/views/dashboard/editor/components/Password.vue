<template>
  <el-form>
    <el-form-item label="原密码">
      <el-input v-model.trim="user.oldPassword" />
    </el-form-item>
    <el-form-item label="新密码">
      <el-input v-model.trim="user.newPassword" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit" icon="el-icon-edit">修改密码</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  data() {
    return {
      currentRole: 'editorDashboard',
      user: {
        oldPassword: '',
        newPassword: '',
      }
    }
  },
  methods: {
    submit() {
      this.$store.dispatch("user/modifyPassword", {oldPassword: this.user.oldPassword, newPassword: this.user.newPassword})
        .then(() => {
          this.$message({
            message: '修改密码成功',
            type: 'success',
            duration: 5 * 1000
          })
        })
    }
  }
}
</script>
