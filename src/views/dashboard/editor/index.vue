<template>
  <div class="app-container">
    <div v-if="user">
      <el-row :gutter="20">

        <el-col :span="6" :xs="24">
          <user-card :user="user" />
        </el-col>

        <el-col :span="18" :xs="24">
          <el-card>
            <el-tabs v-model="activeTab">
              <el-tab-pane label="修改用户名" name="username">
                <username :user="user" />
              </el-tab-pane>
              <el-tab-pane label="修改密码" name="password">
                <password :user="user" />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>

      </el-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import UserCard from './components/UserCard'
import Activity from './components/Activity'
import Password from "./components/Password.vue";
import Username from './components/Username.vue'

export default {
  name: 'Profile',
  components: { UserCard, Username, Password },
  data() {
    return {
      user: {},
      activeTab: 'username'
    }
  },
  computed: {
    ...mapGetters([
      'name',
      'avatar',
      'roles',
      'userid'
    ])
  },
  created() {
    this.getUser()
  },
  methods: {
    getUser() {
      this.user = {
        name: this.name,
        role: this.roles.join(' | '),
        email: 'admin@test.com',
        avatar: this.avatar,
        userid: this.userid,
      }
    }
  }
}
</script>
