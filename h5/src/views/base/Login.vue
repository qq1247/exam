<template>
  <div class="container">
    <el-form
      ref="ruleForm"
      :model="ruleForm"
      status-icon
      :rules="rules"
      class="login-wrap"
    >
      <div class="login-title">欢迎登录</div>
      <el-form-item prop="account">
        <template slot="label">
          <i class="common common-wo" />
        </template>
        <el-input
          v-model="ruleForm.account"
          type="text"
          placeholder="请输入账号"
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item prop="password">
        <template slot="label">
          <i class="common common-lock" />
        </template>
        <el-input
          v-model="ruleForm.password"
          type="password"
          placeholder="请输入密码"
          autocomplete="off"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          class="login-btn"
          @click="login('ruleForm')"
        >登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      ruleForm: {
        account: '',
        password: ''
      },
      rules: {
        account: [{ required: true, message: '账号不能为空', trigger: 'blur' }],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 登录
    login(formName) {
      this.$refs[formName].validate(async(valid) => {
        if (valid) {
          this.$store
            .dispatch('user/login', {
              username: this.ruleForm.account,
              password: this.ruleForm.password
            })
            .then(() => {
              this.$router.replace({
                path: '/'
              })
              this.$message('登录成功！')
            })
        } else {
          this.$message.warning('请核对登录信息')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.container {
  width: 100%;
  height: calc(100vh - 50px);
  background-size: cover;
  justify-content: center;
  align-items: center;
}
.login-wrap {
  position: relative;
  width: 35%;
  height: auto;
  padding: 60px;
  box-sizing: border-box;
  background: #fff;
  border-radius: 3px;
  box-shadow: 0 0 16px 3px rgba(0, 0, 0, 0.05);
  .login-title {
    color: #4a5768;
    font-size: 20px;
    font-weight: bold;
    margin: 0 0 30px;
  }
  .login-btn {
    width: 100%;
    margin-top: 30px;
    height: 45px;
  }
}
/deep/ .el-form-item {
  position: relative;
}
/deep/ .el-form-item__label {
  position: absolute;
  left: 0;
  z-index: 100;
  display: flex;
  justify-content: center;
  align-items: center;
  i {
    font-size: 20px;
  }
}

/deep/
  .el-form-item.is-required:not(.is-no-asterisk)
  > .el-form-item__label:before {
  color: transparent;
}

/deep/ .el-input__suffix {
  color: #1e9fff;
}

/deep/ .el-input__inner {
  padding: 0 15px 0 40px;
}
</style>
