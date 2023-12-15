<template>
  <div class="app-container">
    <el-row>
        <router-link :to="{name: 'AssignHomework', param: this.$route.params}">
            <el-button type="primary" icon="el-icon-s-order">发布作业</el-button>
        </router-link>
        <router-link :to="{name: 'PublishPost', param: this.$route.params}">
          <el-button type="primary" icon="el-icon-s-order">发布讨论贴</el-button>
        </router-link>
    </el-row>
    <el-tabs v-model="activeTab">
        <el-tab-pane label="作业列表" name="waitingForCorrect">
            <correct-homework :homework-list="hwList" />
        </el-tab-pane>
        <el-tab-pane label="学生列表" name="studentList">
            <student-list :student-list="list" />
        </el-tab-pane>
        <el-tab-pane label="课程讨论区" name="discussion">
            <question-list :table-data="postList" />
        </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script>
import CorrectHomework from "./CorrectHomework.vue";
import AssignHomework from "./AssignHomework.vue";
import StudentList from "./StudentList.vue";
import QuestionList from "@/views/TestComments/components/QuestionList.vue";
export default {
    name: "teachingClass",
    components : { CorrectHomework, AssignHomework, StudentList, QuestionList },
    data () {
      return {
        list: [],
        hwList: [],
        postList: [],
        activeTab: "waitingForCorrect"
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      fetchData() {
        this.$store.dispatch('user/getStudentList', this.$route.params.id)
            .then((data) => {
              this.list = data.attendeeList.map(item => ({
                name: item
              }));
            })
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

