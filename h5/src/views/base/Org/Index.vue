<template>
  <div class="container">
    <template v-if="hashChildren">
      <el-form ref="queryForm" :inline="true" :model="queryForm">
        <el-row>
          <el-col :span="20">
            <el-form-item label prop="name">
              <el-input
                v-model="queryForm.name"
                placeholder="请输入名称"
                @focus="queryForm.queryShow = true"
              />
            </el-form-item>
            <el-button
              class="query-search"
              icon="el-icon-search"
              type="primary"
              @click="query"
            >查询</el-button>
          </el-col>
          <el-col :span="4">
            <el-form-item>
              <el-button
                icon="el-icon-tickets"
                size="mini"
                type="primary"
                @click="orgTemplate()"
              >下载模板</el-button>
            </el-form-item>
            <el-form-item>
              <el-upload
                ref="uploadTemplate"
                :limit="1"
                name="files"
                list-type="text"
                :headers="headers"
                action="/api/file/upload"
                :file-list="queryForm.fileList"
                :on-success="uploadSuccess"
              >
                <el-button
                  icon="el-icon-upload2"
                  size="mini"
                  type="primary"
                  @click="() => orgImport"
                >导入</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
        <div v-if="queryForm.queryShow" />
      </el-form>
      <div class="table">
        <el-table
          :data="listpage.list"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
          default-expand-all
          row-key="id"
          style="width: 100%"
        >
          <el-table-column label="名称">
            <template slot-scope="scope">
              <span style="margin-left: 10px">{{ scope.row.name }}</span>
            </template>
          </el-table-column>
          <el-table-column label="所属机构">
            <template slot-scope="scope">
              <span style="margin-left: 10px">{{ scope.row.parentName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="排序">
            <template slot-scope="scope">
              <span style="margin-left: 10px">{{ scope.row.no }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="300">
            <template slot-scope="scope">
              <el-tooltip placement="top" content="添加">
                <i class="common common-add" @click="add(scope.row.id)" />
              </el-tooltip>
              <el-tooltip placement="top" content="修改">
                <i class="common common-edit" @click="edit(scope.row.id)" />
              </el-tooltip>
              <el-tooltip placement="top" content="删除">
                <i class="common common-delete" @click="del(scope.row.id)" />
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </template>
    <router-view v-else />
  </div>
</template>

<script>
import { orgListPage, orgTemplate, orgImport } from 'api/base'

export default {
  data() {
    return {
      headers: {
        Authorization: this.$store.getters.token
      },
      // 列表数据
      listpage: {
        total: 0, // 总条数
        curPage: 1, // 当前第几页
        pageSize: 100, // 每页多少条
        list: [] // 列表数据
      },
      // 查询表单
      queryForm: {
        name: null,
        parentId: null,
        fileList: []
      }
    }
  },
  computed: {
    hashChildren() {
      return !(this.$route.matched.length > 2)
    }
  },
  created() {
    this.init()
  },
  methods: {
    // 查询
    async query() {
      const {
        data: { list }
      } = await orgListPage({
        parentId: this.queryForm.parentId,
        name: this.queryForm.name,
        curPage: this.listpage.curPage,
        pageSize: this.listpage.pageSize
      })

      const treeList = []
      const treeMap = {}
      const idFiled = 'id'
      const parentField = 'parentId'

      for (let i = 0; i < list.length; i++) {
        list[i]['id'] = list[i][idFiled]
        treeMap[list[i]['id']] = list[i]
      }

      for (let i = 0; i < list.length; i++) {
        if (
          treeMap[list[i][parentField]] &&
          list[i]['id'] !== list[i][parentField]
        ) {
          if (!treeMap[list[i][parentField]]['children']) {
            treeMap[list[i][parentField]]['children'] = []
          }
          treeMap[list[i][parentField]]['children'].push(list[i])
        } else {
          treeList.push(list[i])
        }
      }

      this.listpage.list = treeList
    },
    // 初始化
    async init() {
      await this.query()
    },

    // 添加
    add(id) {
      this.$tools.switchTab('OrgIndexSetting', {
        id: 0,
        tab: '1',
        parentId: id
      })
    },
    // 修改
    edit(id) {
      this.$tools.switchTab('OrgIndexSetting', {
        id,
        tab: '1'
      })
    },
    // 删除
    del(id) {
      this.$tools.switchTab('OrgIndexSetting', {
        id,
        tab: '2'
      })
    },
    // 组织机构模板
    async orgTemplate() {
      const template = await orgTemplate({}, 'blob')
      const blob = new Blob([template], {
        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      })
      const url = window.URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = '组织机构模板.xlsx'
      a.click()
      window.URL.revokeObjectURL(url)
    },
    // 上传成功
    uploadSuccess(response, file, fileList) {
      if (this.listpage.list[0]?.children?.length > 1) {
        this.$message.warning('机构已存在，不可导入!')
        this.$refs.uploadTemplate.clearFiles()
        return false
      } else {
        this.orgImport(response.data.fileIds)
      }
    },
    // 组织机构导入
    async orgImport(fileId) {
      const res = await orgImport({
        fileId
      })
      this.$refs.uploadTemplate.clearFiles()
      if (res.code === 200) {
        this.$message.success('导入组织机构成功！')
        this.query()
      }
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

/deep/ .el-upload-list {
  display: none;
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
