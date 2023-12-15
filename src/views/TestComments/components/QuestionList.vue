<template>
  <el-table
    :data="tableData"
    style="width: 100%">
    <el-table-column
      prop="date"
      label="发布日期"
      width="180">
    </el-table-column>
    <el-table-column
      prop="title"
      label="标题">
    </el-table-column>
    <el-table-column
      prop="name"
      label="发布人">
    </el-table-column>
    <el-table-column
      label="操作">
      <template slot-scope="{row,$index}">
      <el-button type="primary" @click="handleClick(row.postid)">查看贴子</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
export default {
  props: {
    tableData: []
  },
  data() {
    return {

    }
  },
  methods: {
    handleClick(postid) {
      this.$router.push({name:'PostDetail',params: {postid: postid, classid: this.$route.params.id}})
    }
  }
}
</script>

<style scoped lang="scss">

</style>
