<template>
  <div class="container">
    <template v-if="hashChildren">
      <el-form ref="queryForm" :inline="true" :model="queryForm">
        <el-row>
          <el-col :span="17">
            <el-form-item label prop="name">
              <el-input v-model="queryForm.name" placeholder="请输入名称" />
            </el-form-item>
            <el-button
              class="query-search"
              icon="el-icon-search"
              type="primary"
              @click="query"
            >查询</el-button>
          </el-col>
          <el-col :span="7">
            <el-form-item style="float: right">
              <el-button
                icon="el-icon-circle-plus-outline"
                size="mini"
                type="primary"
                @click="add"
              >添加</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <div v-if="queryForm.queryShow" />
      </el-form>
      <div class="table">
        <el-table :data="listpage.list" style="width: 100%">
          <el-table-column label="名称" width="120px">
            <template slot-scope="scope">
              <span style="margin-left: 10px">{{ scope.row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" width="70px">
            <template slot-scope="scope">
              <span style="margin-left: 10px">{{ scope.row.stateName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="最近三次触发时间">
            <template slot-scope="scope">
              <span v-for="item in scope.row.triggerTimes" :key="item">
                <el-tag v-if="item" effect="plain" style="margin-bottom: 3px">{{
                  item
                }}</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-tooltip placement="top" content="修改">
                <i class="common common-edit" @click="edit(scope.row.id)" />
              </el-tooltip>
              <el-tooltip placement="top" content="删除">
                <i class="common common-delete" @click="del(scope.row.id)" />
              </el-tooltip>
              <el-tooltip placement="top" content="启动任务">
                <i
                  class="common common-start"
                  @click="startTask(scope.row.id)"
                />
              </el-tooltip>
              <el-tooltip placement="top" content="停止任务">
                <i class="common common-end" @click="stopTask(scope.row.id)" />
              </el-tooltip>
              <el-tooltip placement="top" content="运行一次">
                <i class="common common-once" @click="onceTask(scope.row.id)" />
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-pagination
        :current-page="listpage.curPage"
        :page-size="listpage.pageSize"
        :total="listpage.total"
        background
        hide-on-single-page
        layout="prev, pager, next"
        next-text="下一页"
        prev-text="上一页"
        @current-change="pageChange"
      />
    </template>
    <router-view v-else />
  </div>
</template>

<script>
import { cronListPage } from 'api/base'

export default {
  data() {
    return {
      listpage: {
        // 列表数据
        total: 0, // 总条数
        curPage: 1, // 当前第几页
        pageSize: 10, // 每页多少条
        pageSizes: [10, 20, 50], // 每页多少条
        list: [] // 列表数据
      },
      queryForm: {
        // 查询表单
        name: null
      }
    }
  },
  computed: {
    hashChildren() {
      return !(this.$route.matched.length > 2)
    }
  },
  created() {
    this.query()
  },
  methods: {
    // 查询
    async query(curPage = 1) {
      const {
        data: { list, total }
      } = await cronListPage({
        name: this.queryForm.name,
        curPage: curPage,
        pageSize: this.listpage.pageSize
      })
      this.listpage.total = total
      this.listpage.list = list
    },
    // 添加
    add() {
      this.$tools.switchTab('CronIndexSetting', {
        id: 0,
        tab: '1'
      })
    },
    // 修改
    edit(id) {
      this.$tools.switchTab('CronIndexSetting', {
        id,
        tab: '1'
      })
    },
    // 启动任务
    startTask(id) {
      this.$tools.switchTab('CronIndexSetting', {
        id,
        tab: '2'
      })
    },
    // 停止任务
    stopTask(id) {
      this.$tools.switchTab('CronIndexSetting', {
        id,
        tab: '3'
      })
    },
    // 运行一次
    onceTask(id) {
      this.$tools.switchTab('CronIndexSetting', {
        id,
        tab: '4'
      })
    },
    // 删除
    del(id) {
      this.$tools.switchTab('CronIndexSetting', {
        id,
        tab: '5'
      })
    },
    // 分页切换
    pageChange(val) {
      this.query(val)
    }
  }
}
</script>
<style lang="scss" scoped>
.query-search {
  width: 150px;
  height: 40px;
  border-radius: 5px;
}
.el-input {
  width: 300px;
  float: left;
}
.el-input-number {
  float: left;
}

.el-dialog__title {
  float: left;
}
/deep/ .el-table th {
  background-color: #d3dce6;
  color: #475669;
  text-align: center;
  height: 55px;
}
/deep/ .el-table td {
  color: #8392a6;
  font-size: 12px;
  text-align: center;
  border-bottom: 1px solid #ddd;
}
.common {
  padding: 0 10px;
  color: #0096e7;
  font-size: 18px;
}
.el-link {
  padding-right: 20px;
  color: #8392a6;
  font-size: 12px;
}
/deep/ .el-input__inner:focus {
  box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075),
    0 0 8px rgba(102, 175, 233, 0.6);
  border: 1px solid #f2f4f5;
}
.common-arrow-down {
  margin-left: 10px;
  color: #999;
  font-size: 12px;
}
</style>
