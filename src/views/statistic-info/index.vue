<template>
  <div>
    <el-col>
      <el-card shadow="hover" style="width: 100%;">
        <div slot="header" class="clearfix">
          <span>作业最终得分: {{ score }}</span>
          <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="clickFn"
          >下载批改后作业</el-button
          >
        </div>
      </el-card>
    </el-col>
    <el-col>
      <el-card>
        本次作业分数段统计：
      </el-card>
    </el-col>
    <el-col>
      <el-card>
        <PieChart :chartData="pieChartData" />
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
      score: null,
      pieChartData: { },
    };
  },
  created() {
    this.fetchData()
  },
  methods: {
    transformChartData(dataArray) {
      const scoreSegments = ['60分以下', '70分-80分', '60分-70分','80分-90分', '90分以上'];

      return dataArray.map((value, index) => {
        return {
          value: value,
          name: scoreSegments[index]
        };
      });
    },
    clickFn() {
      this.fetchData()
    },
    add() {
      this.deadline3 = this.deadline3 + 1000 * 10;
    },
    fetchData() {
      this.$store.dispatch('user/getSubmitListStudent', this.$route.params.homeworkid).then((data) => {
        this.score = data.homeworkresult[0].score
      })
      this.$store.dispatch('user/getStatistic', this.$route.params.homeworkid).then((data) => {

        this.pieChartData = this.transformChartData(data.statisticStage)
        console.log(JSON.stringify(this.pieChartData))
      })
    }
  },
};
</script>

<style scoped lang="scss">

</style>
