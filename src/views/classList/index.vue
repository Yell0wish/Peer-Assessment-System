<template>
  <div class="app-container">
    <el-row class="navbar">
      <el-row>
        <el-button @click="showJoinDialog" type="primary" icon="el-icon-s-promotion">加入班级</el-button>
        <el-button @click="showCreateDialog" type="primary" icon="el-icon-s-opportunity">创建班级</el-button>
      </el-row>
    </el-row>
    <el-tabs v-model="activeTab">
      <el-tab-pane label="我教的课" name="teaching">
        <div >
          <div v-if="visibleTeachingCards.length > 0">
            <teaching-class-card :cards-data="visibleTeachingCards" />
          </div>
          <el-pagination
            v-model="currentPageTeaching"
            :page-size="pageSize"
            :total="teachingCards.length"
            layout="total, prev, pager, next, jumper"
            @size-change="handleTeachingSizeChange"
            @current-change="handleTeachingPageChange"
          />
        </div>
      </el-tab-pane>
      <el-tab-pane label="我听的课" name="listening">
        <div>
          <div v-if="visibleListeningCards.length > 0">
            <studying-class-card :cards-data="visibleListeningCards" />
          </div>
          <el-pagination
            v-model="currentPageListening"
            :page-size="pageSize"
            :total="listeningCards.length"
            layout="total, prev, pager, next, jumper"
            @size-change="handleListeningSizeChange"
            @current-change="handleListeningPageChange"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
    <el-dialog :visible.sync="inviteDialogVisible" title="请输入班级邀请码">
      <el-row style="margin-bottom: 20px;">
        <el-input v-model="inviteCode" placeholder="请输入班级邀请码"></el-input>
      </el-row>
      <el-row>
        <el-button @click="confirmJoin" type="primary">确认</el-button>
        <el-button @click="cancelJoin" type="primary">取消</el-button>
      </el-row>
    </el-dialog>
    <el-dialog :visible.sync="createDialogVisible" title="创建课程">
      <el-row style="margin-bottom: 20px;">
        <el-input v-model="classname" placeholder="请输入课程名称"></el-input>
      </el-row>
      <el-row>
        <el-button @click="confirmCreate" type="primary">确认</el-button>
        <el-button @click="cancelCreate" type="primary">取消</el-button>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import TeachingClassCard from "./components/TeachingClassCard.vue";
import StudyingClassCard from "./components/StudyingClassCard.vue";
import {mapGetters} from "vuex";
export default {
  name: 'ClassList',
  components: { StudyingClassCard, TeachingClassCard },
  data() {
    return {
      activeTab: 'teaching', // 默认展示"我教的课"
      inviteDialogVisible: false,
      createDialogVisible: false,
      inviteCode: '',
      classname: '',
      currentPageTeaching: 1,
      currentPageListening: 1,
      pageSize: 4,
      teachingCards: [
        { title: "我教的课1" },
        { title: "我教的课2" },
        { title: "我教的课3" },
        { title: "我教的课4" },
      ],
      listeningCards: [
        { title: "我听的课1" },
        { title: "我听的课2" },
        { title: "我听的课3" },
        { title: "我听的课4" },
        { title: "我听的课5" },
      ]
    };
  },
  computed: {
    visibleTeachingCards() {
      const start = (this.currentPageTeaching - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.teachingCards.slice(start, end);
    },
    visibleListeningCards() {
      const start = (this.currentPageListening - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.listeningCards.slice(start, end);
    }
  },
  created() {

  },
  methods: {
    handleTeachingSizeChange(val) {
      this.pageSize = val;
      this.currentPageTeaching = 1; // Reset to the first page when changing page size
    },
    handleTeachingPageChange(val) {
      this.currentPageTeaching = val;
    },
    handleListeningSizeChange(val) {
      this.pageSize = val;
      this.currentPageListening = 1; // Reset to the first page when changing page size
    },
    handleListeningPageChange(val) {
      this.currentPageListening = val;
    },
    showJoinDialog() {
      this.inviteDialogVisible = true;
    },
    confirmJoin() {
      // 处理确认按钮点击事件
      console.log('确认按钮点击，邀请码为:', this.inviteCode);
      this.inviteDialogVisible = false; // 隐藏 Dialog
    },
    cancelJoin() {
      // 处理取消按钮点击事件
      console.log('取消按钮点击');
      this.inviteDialogVisible = false; // 隐藏 Dialog
    },
    showCreateDialog() {
      this.createDialogVisible = true;
    },
    confirmCreate() {
      // 处理确认按钮点击事件
      console.log('确认按钮点击，邀请码为:', this.inviteCode);
      this.createDialogVisible = false; // 隐藏 Dialog
    },
    cancelCreate() {
      // 处理取消按钮点击事件
      console.log('取消按钮点击');
      this.createDialogVisible = false; // 隐藏 Dialog
    },
  }
}
</script>

<style>
.navbar {
  margin-bottom: 20px;
}
</style>
