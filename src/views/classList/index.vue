<template>
  <div class="app-container">
    <el-row class="navbar">
      <el-row>
        <el-button @click="showJoinDialog" type="primary">加入班级</el-button>
        <el-button @click="showCreateDialog" type="primary">创建班级</el-button>
      </el-row>
    </el-row>
    <div>
      <div v-if="visibleCards.length > 0">
        <class-card :cards-data="visibleCards" />
      </div>
      <el-pagination
        v-model="currentPage"
        :page-size="pageSize"
        :total="cardData.length"
        layout="total,  prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>
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
import ClassCard from "./components/ClassCard.vue";
import {mapGetters} from "vuex";
export default {
  name: 'ClassList',
  components: { ClassCard },
  data() {
    return {
      inviteDialogVisible: false,
      createDialogVisible: false,
      inviteCode: '',
      classname: '',
      currentPage: 1,
      pageSize: 3,
      cardData: [
        {
          title: "测试班级1"
        },
        {
          title: "测试班级2"
        },
        {
          title: "测试班级3"
        },
        {
          title: "测试班级4"
        },
        {
          title: "测试班级5"
        },
      ]
    }
  },
  computed: {
    visibleCards() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.cardData.slice(start, end);
    }
  },
  created() {

  },
  methods: {
    handleSizeChange(val) {
      this.pageSize = val;
      this.currentPage = 1; // Reset to the first page when changing page size
    },
    handlePageChange(val) {
      this.currentPage = val;
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
