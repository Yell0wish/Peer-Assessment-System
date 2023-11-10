<template>
  <div class="app-container">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="待完成作业" name="todo">
        <homework-list />
      </el-tab-pane>
      <el-tab-pane label="已完成作业" name="down">
        <homework-list />
      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script>
import HomeworkList from "./HomeworkList";
export default {
  name: "studyingClass",
  components : { HomeworkList },
  data () {
    return {
      activeTab: "todo"
    }
  }

}

</script>

