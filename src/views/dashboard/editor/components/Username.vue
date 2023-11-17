<template>
  <el-form>
    <el-form-item label="用户名">
      <el-input  v-model.trim="user.name" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submit" icon="el-icon-edit">更改信息</el-button>
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
      this.$message({
        message: 'User information has been updated successfully',
        type: 'success',
        duration: 5 * 1000
      })
    }
  }
}
</script>
