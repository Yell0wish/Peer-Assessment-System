<template>
  <div class="app-container">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="待完成作业" name="todo">
        <homework-list :homework-list="hwList"/>
      </el-tab-pane>
      <el-tab-pane label="已完成作业" name="down">
        <homework-list :homework-list="hwList" />
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
      hwList: [],
      activeTab: "todo"
    }
  },
  created() {
    console.log(this.$route.params.id)
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.$store.dispatch('user/getHomeworkList', this.$route.params.id)
          .then((data) => {
            this.hwList = data.homeworkList.map(item => ({
              timestamp: item.submitTime,
              title: item.title,
              classid: item.classID,
              homeworkid: item.uuid,
            }));
          })
    }
  }
}

</script>

