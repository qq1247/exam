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
            {{ contentIntro }}
            <router-link
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
import Role from './Role.vue'
import Move from './Move.vue'
import Delete from './Deletes.vue'
export default {
  data() {
    return {
      tab: [
        {
          name: '试题分类信息',
          intro: '添加/修改',
          icon: 'common common-edit',
          contentName: '试题分类信息',
          contentIntro:
            '为试题创建一个分类。建议：按类型分开存放，方便管理维护',
          index: '1'
        }
      ],
      contentName: '',
      contentIntro: '',
      contentUrl: '',
      viewList: [Setting, Role, Move, Delete],
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
          name: '操作权限',
          intro: '允许其他子管理员协助添加试题',
          icon: 'common common-role',
          contentName: '操作权限',
          contentIntro:
            '允许其他子管理员协助添加试题。建议：题量大的情况下，添加一个子管理员来协助添加试题，自己做最后的审核（发布）。',
          index: '2'
        },
        {
          name: '移动试题',
          intro: '移动试题到新分类下 ',
          icon: 'common common-move',
          contentName: '移动试题',
          contentIntro: '移动试题到新分类下',
          index: '3'
        },
        {
          name: '删除',
          intro: '删除分类',
          icon: 'common common-delete',
          contentName: '删除',
          contentIntro: '该分类下有试题，则不允许删除',
          index: '4'
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
