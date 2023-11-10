<template>
  <div class="app-container">
    <el-row>
        <router-link to="/assignHomework">
            <el-button type="primary">发布作业</el-button>
        </router-link>
    </el-row>
    <el-tabs v-model="activeTab">
        <el-tab-pane label="待批改作业" name="waitingForCorrect">
            <correct-homework />
        </el-tab-pane>
        <el-tab-pane label="学生列表" name="studentList">
            <student-list />
        </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script>
import CorrectHomework from "./CorrectHomework.vue";
import AssignHomework from "./AssignHomework.vue";
import StudentList from "./StudentList.vue";

export default {
    name: "teachingClass",
    components : { CorrectHomework, AssignHomework, StudentList },
    data () {
        return {
            activeTab: "waitingForCorrect"
        }
    }

}

</script>

