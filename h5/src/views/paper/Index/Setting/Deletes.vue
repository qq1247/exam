<template>
  <el-button type="danger" @click="del">删除</el-button>
</template>

<script>
import { paperTypeDel } from 'api/paper'

export default {
  data() {
    return {
      id: null
    }
  },
  mounted() {
    this.id = this.$route.params.id
  },
  methods: {
    del() {
      this.$confirm(`确认删除吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async() => {
          const res = await paperTypeDel({ id: this.id })
          if (res?.code === 200) {
            this.$message.success('删除成功！')
            this.$router.back()
          } else {
            this.$message.error(res.msg || '删除失败！')
          }
        })
        .catch((err) => {
          console.log(err)
        })
    }
  }
}
</script>
