<template>
  <div class="app-container">
    <router-link :to="{name: 'PublishPost', param: this.$route.params}">
      <el-button type="primary" icon="el-icon-s-order">发布讨论贴</el-button>
    </router-link>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="作业列表" name="todo">
        <homework-list :homework-list="hwList"/>
      </el-tab-pane>
      <el-tab-pane label="课程讨论区" name="discussion">
        <question-list :table-data="postList" />
      </el-tab-pane>
      <el-tab-pane label="课程资源库" name="sources">
        <source-list/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import HomeworkList from "./HomeworkList";
import QuestionList from "@/views/TestComments/components/QuestionList.vue";
import SourceList from "@/views/class-info/student/SourceList.vue";
export default {
  name: "studyingClass",
  components : {SourceList, QuestionList, HomeworkList },
  data () {
    return {
      hwList: [],
      activeTab: "todo",
      postList: [],
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
      this.$store.dispatch('user/getPostList', this.$route.params.id)
          .then((data) => {
            this.postList = data.postList.map(item => ({
              date: item.time,
              title: item.title,
              name: item.name,
              postid: item.uuid,
            }));
          })
    }
  }
}

</script>

