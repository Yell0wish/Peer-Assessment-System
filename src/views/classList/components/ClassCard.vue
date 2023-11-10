<template>
  <el-row>
    <el-col :span="3" v-for="(cardData, index) in cardsData" :key="index" :offset="index > 0 ? 2 : 0">
      <el-card :style="{ width: '300px', height: '450px', border: 'none' }" :body-style="{ padding: '0px' }">
        <img :src="require('@/icons/svg/study.jpg')" class="image" style="width: 100%; height: 100%; object-fit: cover;">
        <div style="padding: 14px;">
          <span>{{ cardData.title }}</span>
          <div class="bottom clearfix">
            <el-button type="text" class="button">进入班级</el-button>
          </div>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script>
export default {
  props: {
    cardsData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      currentDate: new Date()
    };
  }
}
</script>

<style>
/* 省略样式部分 */
</style>
