<template>
  <div class="app-container">
    <el-upload
        class="pop-upload"
        ref="upload"
        action="http://192.168.1.13:8080/"
        :file-list="fileList"
        :auto-upload="false"
        :on-change="handleChange"
        :on-remove="handleRemove"
    >
      <el-button slot="trigger" size="small" type="primary">上传附件</el-button>
      <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">提交资源</el-button>
    </el-upload>
    <el-table :data="submitList" style="width: 73%" border>
      <!-- userid 列 -->
      <el-table-column prop="uuid" label="附件ID" width="180"></el-table-column>

      <!-- time 列 -->
      <el-table-column prop="time" label="上传时间" width="180"></el-table-column>

      <el-table-column prop="name" label="资源名称" width="180"></el-table-column>

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
      fileList: [],
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    handleChange(file, fileList) {
      this.fileList = fileList
    },
    // 删除文件之前的钩子，参数为上传的文件和文件列表，若返回 false 或者返回 Promise 且被 reject，则停止删除。function(file, fileList)
    handleRemove(file, fileList) {
      this.fileList = fileList
    },
    fetchData() {
      console.log(this.$route.params.id)
      this.$store.dispatch('user/getSourceList', this.$route.params.id)
          .then((data) => {
            this.submitList = data.resourceList
            console.log(JSON.stringify(data))
          })
    },
    downloadAttachment(row) {
      // 下载附件的逻辑
      // 你可以在这里添加实际的下载逻辑
      console.log('下载附件:', row.attachmentName);
    },
    submitUpload() {
      console.log(this.$route.params.homeworkid)
      //判断是否有文件再上传
      if (this.fileList.length === 0) {
        return this.$message.warning('请选取文件后再上传')
      }
      //自定义的接口也可以用ajax或者自己封装的接口
      this.$store.dispatch('user/uploadResource', {attachment: this.fileList[0].raw, sourceName: this.fileList[0].raw.name, classid: this.$route.params.id})
          .then((data) => {
            console.log("data:"+ JSON.stringify(data))
          })
    },
  }
}

</script>

