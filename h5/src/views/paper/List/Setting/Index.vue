<template>
  <div class="setting-container">
    <el-tabs v-model="tabIndex" tab-position="right">
      <el-tab-pane v-for="item in tab" :key="item.index" :name="item.index">
        <div slot="label" class="pane-label">
          <i :class="item.icon" />
          <div>
            <div class="label-name">{{ item.name }}</div>
            <div class="label-intro">{{ item.intro }}</div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
    <div class="setting-right">
      <el-card class="box-card" shadow="never">
        <div slot="header">
          <div class="header-name">{{ contentName }}</div>
          <div class="header-intro">
            {{ contentIntro
            }}<router-link
              v-if="contentUrl"
              :to="{ name: contentUrl }"
              class="header-url"
            >去设置</router-link>
          </div>
        </div>
        <component :is="currentView" />
      </el-card>
    </div>
  </div>
</template>

<script>
import Setting from './Setting.vue'
import Copy from './Copy.vue'
import Publish from './Publish.vue'
import Order from './Order.vue'
import Delete from './Deletes.vue'
export default {
  data() {
    return {
      tab: [
        {
          name: '编辑',
          intro: '添加/修改',
          icon: 'common common-edit',
          contentName: '试卷信息',
          contentIntro:
            '阅卷方式选择人工阅卷，发布时检测到所有试题为智能题，则自动变更为智能阅卷。',
          index: '1'
        }
      ],
      contentName: '',
      contentIntro: '',
      contentUrl: '',
      viewList: [Setting, Copy, Publish, Order, Delete],
      currentView: null
    }
  },
  computed: {
    tabIndex: {
      get() {
        return this.$route.params.tab || '1'
      },
      set(val) {
        this.currentView = this.viewList[Number(val) - 1]
        this.contentName = this.tab[Number(val) - 1].contentName
        this.contentIntro = this.tab[Number(val) - 1].contentIntro
        this.contentUrl = this.tab[Number(val) - 1].contentUrl || ''
      }
    }
  },
  created() {
    if (Number(this.$route.params.id)) {
      this.tab = [
        ...this.tab,
        {
          name: '复制',
          intro: '复制试卷',
          icon: 'common common-copy',
          contentName: '复制',
          contentIntro: '复制当前试卷为新试卷（草稿状态）',
          index: '2'
        },
        {
          name: '发布',
          intro: '发布试卷',
          icon: 'common common-publish',
          contentName: '发布',
          contentIntro: '发布后，可被共享权限的用户使用',
          index: '3'
        },
        {
          name: '防作弊',
          intro: '试题乱序、选项乱序',
          icon: 'common common-reset-order',
          contentName: '防作弊',
          contentIntro: '试题乱序、选项乱序',
          index: '4'
        },
        {
          name: '删除',
          intro: '删除试卷',
          icon: 'common common-delete',
          contentName: '删除',
          contentIntro: '不影响关联的试题、考试等，可以正常显示和使用',
          index: '5'
        }
      ]
    }
    this.currentView = this.viewList[Number(this.tabIndex) - 1]
    this.contentName = this.tab[Number(this.tabIndex) - 1].contentName
    this.contentIntro = this.tab[Number(this.tabIndex) - 1].contentIntro
    this.contentUrl = this.tab[Number(this.tabIndex) - 1].contentUrl || ''
  }
}
</script>

<style lang="scss" scoped>
@import 'assets/style/common-setting.scss';
</style>
