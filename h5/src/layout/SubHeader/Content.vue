<template>
  <div class="header-content">
    <el-tabs v-if="tab.length" v-model="tabIndex" @tab-click="changeTab">
      <el-tab-pane
        v-for="item in tab"
        :key="item.name"
        :label="item.meta.title"
        :name="item.name"
      />
    </el-tabs>
    <div v-else>{{ contentName }}</div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      tab: [],
      contentName: '',
      tabIndex: ''
    }
  },
  computed: {
    ...mapGetters(['permission_routes', 'onlyRole'])
  },
  watch: {
    $route: {
      immediate: true,
      deep: true,
      handler(e) {
        this.tabIndex = String(this.$route.name)
        this.getTabInfo(e)
      }
    }
  },
  methods: {
    // 获取路由信息
    getTabInfo(e) {
      const [, oneLevel, twoLevel] = e.path.split('/')
      // 角色匹配路由
      const routes = this.permission_routes.filter(
        (item) => item?.meta?.layout === this.onlyRole[0]
      )
      // 父类路由
      const oneLevelInfo = routes.filter(
        (item) => item.name.toLowerCase() === oneLevel.toLowerCase()
      )
      // 子类路由
      const twoLevelInfo =
        oneLevelInfo.length &&
        oneLevelInfo[0].children.filter(
          (item) => item.path.split('/')[0] === twoLevel
        )

      if (this.$route.matched.length > 2) {
        this.tab = twoLevelInfo[0].children
      } else {
        this.tab = []
        this.contentName = twoLevelInfo[0]?.meta?.title
      }
    },
    changeTab() {
      this.$router.replace({
        name: `${this.tabIndex}`,
        params: this.$route.params
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.header-content {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

/deep/ .el-tabs {
  height: 40px;
}
/deep/ .el-tabs__header {
  margin: 0;
}
/deep/ .el-tabs__nav-wrap::after {
  background-color: #fff;
}

/deep/ .el-tabs__item {
  height: 38px;
  line-height: 40px;
}
</style>
