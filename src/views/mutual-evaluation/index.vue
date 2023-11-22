<template>
  <div>
      <el-row :span="4" offset="2">
        <el-card>
          作业评分：
          <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="clickFn"
          >下载作业</el-button>
          <div style="margin: 10px 0;"></div>
          <el-input
              type="text"
              placeholder="请输入得分"
              v-model="text0"
              show-word-limit
          >
          </el-input>
          <div style="margin: 20px 0;"></div>
          互评评语：
          <div style="margin: 10px 0;"></div>
          <el-input
              type="textarea"
              placeholder="请输入评语"
              v-model="textarea0"
              maxlength="100"
              :rows="6"
              show-word-limit
          >
          </el-input>
          <div style="margin: 10px 0;"></div>
          <el-button type="primary" @click="handleClick">
            提交
          </el-button>
        </el-card>
        <div style="margin: 20px 0;"></div>
        <el-card>
          <div>
            作业评分：
            <el-button
                style="float: right; padding: 3px 0"
                type="text"
                @click="clickFn"
            >下载作业</el-button>
            <div style="margin: 10px 0;"></div>
            <el-input
                type="text"
                placeholder="请输入得分"
                v-model="text1"
                show-word-limit
            >
            </el-input>
          </div>
          <div style="margin: 20px 0;"></div>
          互评评语：
          <div style="margin: 10px 0;"></div>
          <el-input
              type="textarea"
              placeholder="请输入评语"
              v-model="textarea1"
              maxlength="100"
              :rows="6"
              show-word-limit
          >
          </el-input>
          <div style="margin: 10px 0;"></div>
          <el-button type="primary" @click="handleClick">
            提交
          </el-button>
        </el-card>
        <div style="margin: 20px 0;"></div>
        <el-card>
          <div>
            作业评分：
            <el-button
                style="float: right; padding: 3px 0"
                type="text"
                @click="clickFn"
            >下载作业</el-button>
            <div style="margin: 10px 0;"></div>
            <el-input
                type="text"
                placeholder="请输入得分"
                v-model="text2"
                show-word-limit
            >
            </el-input>
          </div>
          <div style="margin: 20px 0;"></div>
          互评评语：
          <div style="margin: 10px 0;"></div>
          <el-input
              type="textarea"
              placeholder="请输入评语"
              :rows="6"
              v-model="textarea2"
              maxlength="100"
              show-word-limit
          >
          </el-input>
          <div style="margin: 10px 0;"></div>
          <el-button type="primary" @click="handleClick">
            提交
          </el-button>
        </el-card>
        <div style="margin: 20px 0;"></div>
        <el-card>
          <div>
            作业评分：
            <el-button
                style="float: right; padding: 3px 0"
                type="text"
                @click="clickFn"
            >下载作业</el-button>
            <div style="margin: 10px 0;"></div>
            <el-input
                type="text"
                placeholder="请输入得分"
                v-model="text3"
                show-word-limit
            >
            </el-input>
          </div>
          <div style="margin: 20px 0;"></div>
          互评评语：
          <div style="margin: 10px 0;"></div>
          <el-input
              type="textarea"
              placeholder="请输入评语"
              v-model="textarea3"
              maxlength="100"
              :rows="6"
              show-word-limit
          >
          </el-input>
          <div style="margin: 10px 0;"></div>
          <el-button type="primary" @click="handleClick">
            提交
          </el-button>
        </el-card>
        <div style="margin: 20px 0;"></div>
        <el-card>
          <div>
            作业评分：
            <el-button
                style="float: right; padding: 3px 0"
                type="text"
                @click="clickFn"
            >下载作业</el-button>
            <div style="margin: 10px 0;"></div>
            <el-input
                type="text"
                placeholder="请输入得分"
                v-model="text4"
                show-word-limit
            >
            </el-input>
          </div>
          <div style="margin: 20px 0;"></div>
          互评评语：
          <div style="margin: 10px 0;"></div>
          <el-input
              type="textarea"
              placeholder="请输入评语"
              v-model="textarea4"
              :rows="6"
              maxlength="100"
              show-word-limit
          >
          </el-input>
          <div style="margin: 10px 0;"></div>
          <el-button type="primary" @click="handleClick">
            提交
          </el-button>
        </el-card>
      </el-row>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        text0: '',
        text1: '',
        text2: '',
        text3: '',
        text4: '',
        textarea0: '',
        textarea1: '',
        textarea2: '',
        textarea3: '',
        textarea4: '',
      }
    },
    methods: {
      clickFn() {
        window.location.href = 'http://localhost:8080/甘特图.pdf'
      },
      handleClick() {
        this.$message({
          type: 'success',
          message: '提交成功！'
        });
      },

    },

  }

</script>

<style scoped lang="scss">

</style>
