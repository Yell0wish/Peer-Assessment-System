<template>
  <el-form>
    <el-form-item label="用户名">
      <el-input  v-model.trim="user.name" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit" icon="el-icon-edit">修改用户名</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          name: '',
          email: ''
        }
      }
    }
  },
  methods: {
    submit() {
      console.log(this.user.name)
      this.$store.dispatch("user/modifyPassword", this.user.name)
      this.$message({
        message: '成功修改用户名',
        type: 'success',
        duration: 5 * 1000
      })
    }
  }
}
</script>
