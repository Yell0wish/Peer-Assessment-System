<template>
  <div class="page-box">
    <el-card>
      <el-upload
          class="pop-upload"
          ref="upload"
          action="http://192.168.1.13:8080/"
          :file-list="fileList"
          :auto-upload="false"
          :multiple="true"
          :on-change="handleChange"
          :on-remove="handleRemove"
      >
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
      </el-upload>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request"    // 此处为请求的封装方法，没有引用可以忽略
export default {
  data() {
    return {
      fileList: [],   // 定义一个空数组
    };
  },
  methods: {
    // 文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用,function(file, fileList)
    handleChange(file, fileList) {
      this.fileList = fileList
    },
    // 删除文件之前的钩子，参数为上传的文件和文件列表，若返回 false 或者返回 Promise 且被 reject，则停止删除。function(file, fileList)
    handleRemove(file, fileList) {
      this.fileList = fileList
    },
    //上传服务器
    submitUpload() {
      //判断是否有文件再上传
      if (this.fileList.length === 0) {
        return this.$message.warning('请选取文件后再上传')
      }
      // 下面的代码将创建一个空的FormData对象:
      const formData = new FormData()
      // 你可以使用FormData.append来添加键/值对到表单里面；
      formData.append('fileA', this.fileList[0].raw);
      formData.append('fileB', this.fileList[1].raw);


      //自定义的接口也可以用ajax或者自己封装的接口
      request({
        method: 'POST',
        url: '/ContentCheck',   //填写自己的接口
        data: formData,       //填写包装好的formData对象
        headers: {
          'Access-Control-Allow-Origin': '*'
        },
      }).then(res => {
        window.console.log(JSON.stringify(res.data))
        if (res.code === 200) {
          this.$message.success('上传成功');
          // this.$store.dispatch("user/getDuplicateReport", res.data.token)
          const downloadLink = document.createElement('a');
          downloadLink.href = `http://192.168.215.88:8080/getContentCheck?checkToken=${res.data.token}`; // 替换为实际的文件下载链接
          downloadLink.download = '查重报告';
          downloadLink.click();
        } else {
          this.$message.error('上传失败');
        }
        //清空fileList
        this.fileList = []
      })
    },
  },
};
</script>

<style>
  .page-box{
    display: flex;
    align-items: center;
    justify-content: center;
    .el-card{
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }
</style>
