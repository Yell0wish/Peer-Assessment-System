<template>
  <div>
    <el-col>
      <el-card shadow="hover" style="width: 100%;">
        <div slot="header" class="clearfix">
          <span>作业评语</span>
          <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="clickFn"
          >下载批改后作业</el-button
          >
        </div>
        <div style="font-size: 18px;text-align: center; margin-top: 35px;">
          明日复明日
        </div>
        <div style="font-size: 18px;text-align: center;">明日何其多</div>
        <div style="font-size: 18px;text-align: center;">我生待明日</div>
        <div style="font-size: 18px;text-align: center;">万事成蹉跎</div>
        <div style="margin-top: 35px;"></div>
      </el-card>
    </el-col>
    <el-col>
      <el-card>
        <PieChart></PieChart>
      </el-card>
    </el-col>
    <el-col>
      <el-card>
        <line-chart :chart-data="lineChartData" />
      </el-card>
    </el-col>
  </div>
</template>

<script>
import PieChart from './components/PieChart'
import LineChart from './components/LineChart'
const lineChartData = {
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130]
  },
  purchases: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130]
  },
  shoppings: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130]
  },
  correctRate: {
    expectedData: [100, 90, 80, 90, 95, 100, 65, 95],
    actualData: [90, 82, 91, 94, 82, 100, 45, 100]
  },
}
export default {
  components: {LineChart, PieChart},
  data() {
    return {
      deadline2: Date.now() + 1000 * 60 * 60 * 8,
      deadline3: Date.now() + 1000 * 60 * 30,
      deadline4: Date.now() + (new Date().setHours(23, 59, 59) - Date.now()),
      deadline5: new Date("2023-05-06"),
      stop: true,
      lineChartData: lineChartData.correctRate
    };
  },
  methods: {
    clickFn() {
      window.location.href = 'http://localhost:8080/甘特图.pdf'
    },
    add() {
      this.deadline3 = this.deadline3 + 1000 * 10;
    },
  },
};
</script>

<style scoped lang="scss">

</style>
