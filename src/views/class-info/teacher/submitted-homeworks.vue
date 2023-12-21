<template>
  <div class="app-container">
    <el-table :data="submitList" style="width: 53%" border>
      <!-- userid 列 -->
      <el-table-column prop="userID" label="用户ID" width="180"></el-table-column>

      <!-- time 列 -->
      <el-table-column prop="time" label="提交时间" width="180"></el-table-column>

      <!-- 操作列 -->
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <!-- 下载按钮 -->
          <el-button size="small" @click="downloadAttachment(scope.row)" type="primary">下载附件</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import CorrectHomework from "./CorrectHomework.vue";
import AssignHomework from "./AssignHomework.vue";
import StudentList from "./StudentList.vue";
import QuestionList from "@/views/TestComments/components/QuestionList.vue";
export default {
  name: "SubmittedHomeworks",
  data () {
    return {
      list: [],
      hwList: [],
      postList: [],
      activeTab: "waitingForCorrect",
      submitList: [],
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      console.log(this.$route.params.homeworkid)
      this.$store.dispatch('user/getSubmitList', this.$route.params.homeworkid)
          .then((data) => {
            this.submitList = data.submitList;
            console.log(JSON.stringify(data))
          })
    },
    downloadAttachment(row) {
      // 下载附件的逻辑
      // 你可以在这里添加实际的下载逻辑
      console.log('下载附件:', row.attachmentName);
    },
  }
}

</script>

