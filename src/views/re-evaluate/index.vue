<template>
  <div class="app-container">
    <el-table :data="allocatedList" style="width: 72%" border>

      <el-table-column prop="submitID" label="用户ID" width="180"></el-table-column>
      <el-table-column prop="time" label="评分截止时间" width="180"></el-table-column>
      <el-table-column prop="uuid" label="作业ID" width="180"></el-table-column>

      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
          <!-- 下载按钮 -->
          <el-button size="small" @click="downloadAttachment(scope.row)" type="primary">下载作业附件</el-button>
          <el-button size="small" @click="handleClick(scope.row)" type="primary">评分</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogVisible" title="输入分数和评语" width="50%">
      <el-form ref="form" :model="form" label-width="120px">
        <el-row>
          <!-- 整数输入框 -->
          <el-col :span="24">
            <el-form-item label="分数">
              <el-input-number
                  v-model="form.score"
                  :min="1"
                  :max="100"
                  label="请输入1-100之间的整数">
              </el-input-number>
            </el-form-item>
          </el-col>

          <!-- 评语输入框 -->
          <el-col :span="24">
            <el-form-item label="评语">
              <el-input
                  type="textarea"
                  v-model="form.comment"
                  :rows="4"
                  maxlength="150"
                  show-word-limit
                  placeholder="请输入评语（最多150字）">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 提交按钮 -->
        <el-form-item>
          <el-button type="primary" @click="onSubmit">提交</el-button>
          <el-button @click="dialogVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "SubmittedHomeworks",
  data () {
    return {
      list: [],
      hwList: [],
      postList: [],
      activeTab: "waitingForCorrect",
      allocatedList: [],
      currentHomeworkId: null,
      dialogVisible: false, // 控制对话框显示
      form: {
        score: 1 // 分数输入模型
      },
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    onSubmit() {
      console.log('输入的分数:', this.form.score);
      console.log('输入的评语:', this.form.comment);
      console.log('作业 ID:', this.currentHomeworkId);
      // 在这里添加提交逻辑
      this.dialogVisible = false;
      this.$store.dispatch('user/reCorrectHomework', {correctid: this.currentHomeworkId, score: this.form.score, comment: this.form.comment})
    },
    fetchData() {
      console.log(this.$route.params.homeworkid)
      this.$store.dispatch('user/getReassessmentList')
          .then((data) => {
            this.allocatedList = data.reassessmentList;
            console.log(JSON.stringify(data))
          })
    },
    downloadAttachment(row) {
      // 下载附件的逻辑
      // 你可以在这里添加实际的下载逻辑
      console.log('下载附件:', row.attachmentName);
    },
    handleClick(row) {
      this.currentHomeworkId = row.uuid; // 保存当前作业的 ID
      console.log('作业 ID:', this.currentHomeworkId);
      this.dialogVisible = true;
    },
  }
}

</script>

