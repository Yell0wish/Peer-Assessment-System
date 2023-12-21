<template>
  <div class="app-container">
<!--    <el-table v-loading="listLoading" :data="studentList" border style="width: 100%">-->

<!--      &lt;!&ndash; Author Column &ndash;&gt;-->
<!--      <el-table-column prop="name" label="Author" width="120px" align="center"></el-table-column>-->

<!--      &lt;!&ndash; Actions Column &ndash;&gt;-->
<!--      <el-table-column label="Actions" width="180px" align="center">-->
<!--        <template slot-scope="scope">-->
<!--          <router-link :to="'/example/edit/'+scope.row.id">-->
<!--            <el-button type="danger" size="small" icon="el-icon-error">-->
<!--              踢出班级-->
<!--            </el-button>-->
<!--          </router-link>-->
<!--        </template>-->
<!--      </el-table-column>-->

<!--    </el-table>-->

<!--    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />-->
    <el-table
      :data="studentList"
      border
      style="width: 25%">
      <el-table-column
        prop="name"
        label="姓名"
        width="200">
      </el-table-column>
      <el-table-column label="操作" width="200">
        <el-button type="danger" size="small" icon="el-icon-error" on-buttong>
          踢出班级
        </el-button>
      </el-table-column>
    </el-table>
  </div>
</template>


<script>
import { fetchList } from '@/api/article'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination

export default {
  name: 'ArticleList',
  components: { Pagination },
  props: {
    studentList: null
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      studentList: null,
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 20
      }
    }
  },
  created() {
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.list = response.data.items
        this.total = response.data.total
        this.listLoading = false
      })
    }
  }
}
</script>

<style scoped>
.edit-input {
  padding-right: 100px;
}
.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>
