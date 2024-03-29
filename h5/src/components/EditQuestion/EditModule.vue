<template>
  <el-form
    ref="editForm"
    size="mini"
    class="edit-form"
    label-width="80px"
    :model="editForm"
    :rules="editForm.rules"
  >
    <el-form-item label="难度" prop="difficulty">
      <el-select
        v-model="editForm.difficulty"
        clearable
        placeholder="请选择难度"
      >
        <el-option
          v-for="dict in editForm.difficultyList"
          :key="parseInt(dict.dictKey)"
          :label="dict.dictValue"
          :value="parseInt(dict.dictKey)"
        />
      </el-select>
    </el-form-item>

    <el-form-item label="题干" prop="title">
      <TinymceEditor
        id="title"
        :value="editForm.title"
        @editorListener="editorListener"
      />
    </el-form-item>

    <div v-if="editForm.type === 3" class="cloze-tip">
      五个下划线（_____ ）表示一个填空
    </div>

    <!-- 判断，多选的选项 -->
    <div v-if="[1, 2].includes(editForm.type)">
      <el-form-item
        v-for="(option, index) in editForm.options"
        :key="`option${option.lab}`"
        :label="`选项${option.lab}`"
        :prop="`options.${index}.value`"
        :rules="editForm.rules.option"
      >
        <TinymceEditor
          :id="'option' + option.lab"
          :value="option.value"
          @editorListener="editorListener"
        />
      </el-form-item>
      <!-- 选项按钮 -->
      <el-form-item>
        <el-button
          :disabled="editForm.options.length >= 7"
          class="option-btn option-btn-primary"
          type="primary"
          @click="addOption(editForm.options.length, '')"
        ><img
          src="@/assets/img/question/question-plus.png"
          alt=""
        >新增选项</el-button>
        <el-button
          :disabled="editForm.options.length <= 2"
          class="option-btn option-btn-danger"
          type="danger"
          @click="delOption"
        ><img
          src="@/assets/img/question/question-minus.png"
          alt=""
        >删除选项</el-button>
      </el-form-item>
    </div>

    <!-- AI，答案选项 -->
    <el-row>
      <el-col :span="2.5">
        <div v-if="[3, 5].includes(editForm.type)">
          <el-form-item label="智能阅卷" prop="ai">
            <el-switch
              v-model="editForm.ai"
              active-color="#0094e5"
              :active-value="1"
              inactive-color="#ff4949"
              :inactive-value="2"
            />
          </el-form-item>
        </div>
      </el-col>
      <el-col :span="18">
        <div v-if="editForm.ai === 1 && editForm.type === 3">
          <el-form-item prop="aiOptions">
            <el-checkbox-group v-model="editForm.aiOptions">
              <el-tooltip
                class="item"
                content="默认答案有顺序"
                effect="dark"
                placement="top"
              >
                <el-checkbox :label="2">答案无顺序</el-checkbox>
              </el-tooltip>
              <el-tooltip
                class="item"
                content="默认大小写敏感"
                effect="dark"
                placement="top"
              >
                <el-checkbox :label="3">大小写不敏感</el-checkbox>
              </el-tooltip>
            </el-checkbox-group>
          </el-form-item>
        </div>
        <div v-if="editForm.ai === 1 && editForm.type === 5">
          <el-form-item prop="aiOptions">
            <el-checkbox-group v-model="editForm.aiOptions">
              <el-tooltip
                class="item"
                content="大小写不敏感"
                effect="dark"
                placement="top"
              >
                <el-checkbox :label="3">大小写不敏感</el-checkbox>
              </el-tooltip>
            </el-checkbox-group>
          </el-form-item>
        </div>
      </el-col>
    </el-row>

    <!-- 分值 -->
    <el-row>
      <el-col :span="12">
        <el-form-item label="分值" prop="score">
          <el-input-number
            v-model.number="editForm.score"
            :max="100"
            :min="1"
            :step="1"
            controls-position="right"
          />
        </el-form-item>
      </el-col>
      <el-col v-if="editForm.type === 2" :span="12">
        <el-form-item prop="multipScore">
          <div class="lose">
            漏选得
            <el-input v-model="editForm.multipScore" size="mini" />分
          </div>
        </el-form-item>
      </el-col>
    </el-row>

    <!-- 状态 -->
    <el-form-item label="状态">
      <el-radio-group v-model="editForm.state">
        <el-radio
          v-for="state in editForm.states"
          :key="state.value"
          :label="state.value"
        >{{ state.lab }}</el-radio>
      </el-radio-group>
    </el-form-item>

    <!-- 答案 -->
    <el-form-item
      v-if="editForm.type === 1"
      key="answerRadio"
      label="答案"
      prop="answer"
    >
      <el-radio-group v-model="editForm.answer">
        <el-radio
          v-for="answer in editForm.answers"
          :key="answer.lab"
          :label="answer.lab"
        >{{ answer.lab }}</el-radio>
      </el-radio-group>
    </el-form-item>

    <el-form-item
      v-if="editForm.type === 2"
      key="answerCheckbox"
      label="答案"
      prop="answerMultip"
    >
      <el-checkbox-group v-model="editForm.answerMultip">
        <el-checkbox
          v-for="answer in editForm.answers"
          :key="answer.lab"
          :label="answer.lab"
        >{{ answer.lab }}</el-checkbox>
      </el-checkbox-group>
    </el-form-item>

    <el-form-item v-if="editForm.type === 3" key="answerCloze" label="答案">
      <el-card class="el-card" shadow="never">
        <el-alert
          :closable="false"
          title="单个填空有多个同义词，可添加多个标签。如：人民、公民、群众"
          type="success"
        />
        <el-row
          v-for="(answer, index) in editForm.answers"
          :key="index"
          :gutter="10"
        >
          <el-col :span="16">
            <el-form-item
              :prop="`answers.${index}.value`"
              :rules="editForm.rules.answerMultip"
            >
              <span
                style="margin: 0 10px; color: #838ee9; font-size: 12px"
              >填空{{ $tools.intToChinese(index + 1) }}</span>
              <el-select
                v-model="answer.value"
                class="custom-select"
                remote
                multiple
                clearable
                filterable
                allow-create
                default-first-option
                placeholder="请填写答案"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              :prop="`answers.${index}.score`"
              :rules="editForm.rules.aiScore"
              :show-message="editForm.ai === 1 ? true : false"
            >
              <el-input v-if="editForm.ai === 1" v-model="answer.score">
                <div slot="append">分</div>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
    </el-form-item>

    <el-form-item
      v-if="editForm.type === 4"
      key="answerJudge"
      label="答案"
      prop="answer"
    >
      <el-radio-group v-model="editForm.answer">
        <el-radio
          v-for="answer in editForm.judgeAnswers"
          :key="answer.lab"
          :label="answer.lab"
        >{{ answer.lab }}</el-radio>
      </el-radio-group>
    </el-form-item>

    <el-form-item
      v-if="editForm.type === 5 && editForm.ai === 1"
      key="answerAskAI"
      label="答案"
    >
      <el-card class="el-card" shadow="never">
        <el-alert
          :closable="false"
          title="单个关键词有多个同义词，可添加多个标签。如：人民、公民、群众"
          type="success"
        />
        <el-row
          v-for="(answer, index) in editForm.answers"
          :key="index"
          :gutter="10"
        >
          <el-col :span="16">
            <el-form-item
              :prop="`answers.${index}.value`"
              :rules="editForm.rules.answerMultip"
            >
              <span
                style="margin: 0 10px; color: #838ee9; font-size: 12px"
              >关键词{{ $tools.intToChinese(index + 1) }}</span>
              <el-select
                v-model="answer.value"
                class="custom-select"
                remote
                multiple
                clearable
                filterable
                allow-create
                default-first-option
                placeholder="请填写答案"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              :prop="`answers.${index}.score`"
              :rules="editForm.rules.aiScore"
              :show-message="editForm.ai === 1 ? true : false"
            >
              <el-input v-if="editForm.ai === 1" v-model="answer.score">
                <div slot="append">分</div>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-button
          :disabled="editForm.answers.length >= 7"
          class="option-btn option-btn-primary"
          style="margin-left: 65px"
          type="primary"
          @click="addFillBlanks(editForm.answers.length, '')"
        ><img
          src="@/assets/img/question/question-plus.png"
          alt=""
        >添加关键词</el-button>
        <el-button
          :disabled="editForm.answers.length <= 1"
          class="option-btn option-btn-danger"
          type="danger"
          @click="delFillBlanks"
        ><img
          src="@/assets/img/question/question-minus.png"
          alt=""
        >删除关键词</el-button>
      </el-card>
    </el-form-item>

    <el-form-item
      v-if="editForm.type === 5 && editForm.ai === 2"
      key="answerAsk"
      label="答案"
      prop="answer"
    >
      <TinymceEditor
        v-if="editForm.ai === 2"
        id="answer"
        :value="editForm.answer"
        @editorListener="editorListener"
      />
    </el-form-item>

    <el-form-item label="解析" prop="analysis">
      <TinymceEditor
        id="analysis"
        :value="editForm.analysis"
        @editorListener="editorListener"
      />
    </el-form-item>

    <el-form-item>
      <el-button
        v-if="!questionId"
        style="width: 164px; height: 40px"
        type="primary"
        @click="add()"
      >添加</el-button>
      <el-button
        v-if="questionId"
        style="width: 164px; height: 40px"
        type="primary"
        @click="edit()"
      >修改</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import TinymceEditor from 'components/TinymceEditor/Index.vue'
import { getOneDict } from '@/utils/getDict'
import { questionGet } from 'api/question'
export default {
  components: {
    TinymceEditor
  },
  props: {
    questionType: {
      type: Number,
      default: 1
    },
    questionTypeId: {
      type: Number,
      default: null
    },
    questionId: {
      type: Number,
      default: null
    },
    publish: {
      type: Boolean,
      default: false
    }
  },
  data() {
    const validateAiScore = (rule, value, callback) => {
      if (this.editForm.ai === 2) {
        return callback()
      }
      if (value === '') {
        return callback(new Error('请填写分数'))
      }

      const allScore = this.editForm.answers.reduce(
        (acc, cur) => acc + Number(cur.score),
        0
      )

      if (this.editForm.ai === 1 && this.editForm.score < allScore) {
        return callback(new Error(`答案总分值大于${this.editForm.score}`))
      }

      if (this.editForm.ai === 1 && this.editForm.score > allScore) {
        return callback(new Error(`答案总分值小于${this.editForm.score}`))
      }

      return callback()
    }

    const validateMultipScore = (rule, value, callback) => {
      if (value === '') {
        return callback(new Error('请填写分数'))
      }
      if (value >= this.editForm.score) {
        return callback(new Error(`漏选分值应小于总分`))
      }
      if (value < 0) {
        return callback(new Error(`漏选分值不能小于0分`))
      }
      return callback()
    }
    return {
      editForm: {
        // 修改表单
        id: null, // 主键
        type: 1, // 类型
        difficulty: 1, // 难度
        difficultyList: [],
        title: '', // 标题
        ai: 1, // AI阅卷
        state: 1,
        options: [
          {
            lab: 'A',
            value: ''
          },
          {
            lab: 'B',
            value: ''
          }
        ], // 选项
        answer: '', // 答案
        answerMultip: [],
        answers: [
          {
            lab: 'A',
            value: [],
            score: ''
          },
          {
            lab: 'B',
            value: [],
            score: ''
          }
        ],
        states: [
          {
            lab: '发布',
            value: 1
          },
          {
            lab: '草稿',
            value: 2
          }
        ],
        // 答案
        judgeAnswers: [
          {
            lab: '对',
            value: ''
          },
          {
            lab: '错',
            value: ''
          }
        ],
        analysis: '', // 解析
        score: 1, // 分值
        multipScore: 0.5,
        aiOptions: [],
        rules: {
          title: [{ required: true, message: '请输入题干', trigger: 'blur' }],
          option: [
            { required: true, message: '请输入选项内容', trigger: 'change' }
          ],
          score: {
            required: true,
            message: '请输入分值',
          },
          answer: [
            {
              required: true,
              message: '请选择或者输入答案',
              trigger: 'blur'
            }
          ],
          answerMultip: [
            {
              type: 'array',
              required: true,
              message: '请选择或者输入答案',
              trigger: 'blur'
            }
          ],
          aiScore: [{ validator: validateAiScore }],
          multipScore: [
            { required: true, trigger: 'blur', validator: validateMultipScore }
          ]
        }
      }
    }
  },
  watch: {
    questionType(n) {
      this.editForm.type = n
      this.resetData()
      this.editForm.options.length = 2
      this.editForm.answers =
        this.editForm.type === 3
          ? []
          : [
            {
              lab: 'A',
              value: [],
              score: ''
            },
            {
              lab: 'B',
              value: [],
              score: ''
            }
          ]
    },
    questionId(n) {
      if (n) {
        this.getQuestionDetail(n)
      }
    },
    'editForm.score': {
      deep: true,
      immediate: true,
      handler(newValue) {
        if (this.editForm.type === 2) {
          this.editForm.multipScore = newValue / 2
        }
      }
    }
  },
  created() {
    this.editForm.difficultyList = getOneDict('QUESTION_DIFFICULTY')
  },
  methods: {
    // 编辑器监听事件
    editorListener(id, value) {
      if (id.startsWith('option')) {
        const index = parseInt(id.substr(6).charCodeAt() - 65) // 选项赋值，特殊处理一下
        this.editForm.options[index].value = value
        return
      }
      this.editForm[id] = value

      if (this.editForm.type === 3 && id === 'title') {
        const underlineNum = value.match(/[_]{5,}/g)

        // 下划线不存在，则答案选项置空
        if (!underlineNum) {
          this.editForm.answers = []
          return false
        }

        // 下划线数量大于现有答案选项数量，则进行补充填空
        if (underlineNum.length > this.editForm.answers.length) {
          for (
            let index = this.editForm.answers.length;
            index < underlineNum.length;
            index++
          ) {
            const lab = this.$tools.intToChinese(index + 1)
            this.editForm.answers.push({
              lab,
              value: [],
              score: ''
            })
          }
        }

        // 下划线数量小于现有答案选项数量，则删除填空
        if (underlineNum.length < this.editForm.answers.length) {
          this.editForm.answers.length = underlineNum.length
        }
      }
    },
    // 添加选项
    addOption(index, value) {
      const lab = String.fromCharCode(65 + index)

      this.editForm.options.push({ lab, value })
      this.editForm.answers.push({ lab, value })
    },
    // 删除选项
    delOption() {
      this.editForm.options.pop()
      this.editForm.answers.pop()
    },
    // 添加填空
    addFillBlanks(index, value) {
      const lab = this.$tools.intToChinese(index)

      this.editForm.answers.push({
        lab: lab,
        value: !value ? '' : value.answer,
        score: !value ? '' : value.score
      })
    },
    // 删除填空
    delFillBlanks() {
      this.editForm.answers.pop()
    },
    // 获取试题编辑详情
    async getQuestionDetail(id) {
      const res = await questionGet({ id })
      if (res?.code !== 200) {
        this.$message.error('查询失败！')
        return
      }

      this.editForm.id = res.data.id
      this.editForm.type = res.data.type
      this.editForm.difficulty = res.data.difficulty
      this.editForm.title = res.data.title
      this.editForm.answer = res.data.answers[0].answer[0]
      this.editForm.analysis = res.data.analysis
      this.editForm.score = res.data.score
      this.editForm.ai = res.data.ai
      this.editForm.state = res.data.state

      if (this.editForm.type === 1) {
        this.editForm.options = [] // 重置选项
        this.editForm.answers = [] // 重置答案列表
        for (let i = 0; i < res.data.options.length; i++) {
          this.addOption(i, res.data.options[i])
        }
      }

      if (this.editForm.type === 2) {
        this.editForm.answerMultip = []
        this.editForm.multipScore = ''
        this.editForm.options = [] // 重置选项
        this.editForm.answers = [] // 重置答案列表
        for (let i = 0; i < res.data.options.length; i++) {
          this.addOption(i, res.data.options[i])
        }
        this.editForm.aiOptions = res.data.aiOptions
        this.editForm.answerMultip = res.data.answers.reduce((acc, cur) => {
          acc.push(...cur.answer)
          return acc
        }, [])
        this.editForm.multipScore = res.data.answers[0].score
      }

      if (this.editForm.type === 5 && this.editForm.ai === 2) {
        this.editForm.answers = [] // 重置答案列表
      }

      if (
        this.editForm.type === 3 ||
        (this.editForm.type === 5 && this.editForm.ai === 1)
      ) {
        this.$nextTick(() => {
          const answers = res.data.answers
          this.editForm.answers = [] // 重置答案列表
          for (let i = 0; i < answers.length; i++) {
            this.addFillBlanks(i, answers[i])
          }
          this.editForm.aiOptions = res.data.aiOptions
        })
      }

      if (res.data.ai === 1 && res.data.type === 5) {
        this.editForm.answer = ''
      }
    },
    // 组合添加或修改请求参数
    compositionParam(status) {
      const params = {
        type: this.editForm.type,
        difficulty: this.editForm.difficulty,
        title: this.editForm.title,
        analysis: this.editForm.analysis,
        score: this.editForm.score,
        ai: this.editForm.ai,
        state: this.editForm.state
      }

      status
        ? ((params.questionTypeId = this.questionTypeId),
        (params.state = this.publish ? 1 : this.editForm.state))
        : ((params.id = this.editForm.id), (params.state = this.editForm.state))

      // 选项值(单选、多选)
      if ([1, 2].includes(params.type)) {
        params.options = this.editForm.options.reduce((acc, cur) => {
          acc.push(cur.value)
          return acc
        }, [])
      }

      // 分值选项（多选【漏选得分】）
      if (params.type === 2) {
        params.aiOptions = [1]
      }

      // 分值选项 ( 填空【漏答得分、大小写不敏感】  问答【大小写不敏感】)
      if ([3, 5].includes(params.type) && params.ai === 1) {
        params.aiOptions = this.editForm.aiOptions.reduce((acc, cur) => {
          acc.push(cur)
          return acc
        }, [])
      }

      // 答案（单选、判断、问答【非智能 ai=2】）
      if ([1, 4, 5].includes(params.type)) {
        params.answers = this.editForm.answer
      }

      // 答案（多选）
      if (params.type === 2) {
        params.answers = this.editForm.answerMultip
      }

      // 答案（填空、问答【智能 ai=1】）
      if (params.type === 3 || (params.ai === 1 && params.type === 5)) {
        params.answers = this.editForm.answers.reduce((acc, cur) => {
          acc.push(cur.value.join('\n'))
          return acc
        }, [])
      }

      // 分值选项对应的分值（多选）
      if (params.type === 2) {
        params.answerScores = this.editForm.multipScore
      }

      // 分值选项对应的分值（填空、问答）
      if ([3, 5].includes(params.type) && params.ai === 1) {
        params.answerScores = this.editForm.answers.reduce((acc, cur) => {
          acc.push(params.ai === 1 ? cur.score : params.score)
          return acc
        }, [])
      }

      return params
    },
    // 添加试题
    add() {
      const params = this.compositionParam(true)
      if (!params) return

      this.$refs['editForm'].validate(async(valid) => {
        if (!valid) {
          return false
        }
        this.$emit('add', params)
      })
    },
    // 修改试题
    edit() {
      const params = this.compositionParam(false)
      if (!params) return

      this.$refs['editForm'].validate((valid) => {
        if (!valid) {
          return false
        }
        this.$confirm('确定要修改？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(async() => {
            this.$emit('edit', params)
          })
          .catch((err) => {
            console.log(err)
          })
      })
    },
    // 还原表单数据
    resetData() {
      this.$refs['editForm'].resetFields()
    }
  }
}
</script>

<style lang="scss" scoped>
.option-btn {
  border: none;
  padding: 8px;
  img {
    vertical-align: -3px;
    margin-right: 4px;
  }
}

.option-btn-primary {
  background: rgba(#0094e5, 0.1);
  color: #0094e5;
}

.option-btn-danger {
  background: rgba(#eb5b5b, 0.1);
  color: #eb5b5b;
}

/deep/ .el-card__body {
  padding: 5px;
}

.lose {
  display: flex;
  align-items: flex-end;
  height: 30px;
  /deep/ .el-input {
    width: 40px;
  }
  /deep/ .el-input__inner {
    border: none;
    border-bottom: 1px solid #dcdfe6;
    text-align: center;
    padding: 0 7px;
    height: 20px;
    line-height: 20px;
    border-radius: 0;
  }
}

.el-alert--success.is-light {
  background-color: #ecf7fd;
  color: #0094e5;
  margin-bottom: 10px;
}

.el-alert {
  padding: 0px;
}

/deep/ .el-alert__title {
  font-size: 12px;
  color: #0094e5;
}

.cloze-tip {
  color: #0094e5;
  padding-left: 80px;
  margin-bottom: 10px;
  font-size: 12px;
}

.custom-select {
  width: 120px;
}

/deep/.el-select__tags .el-tag {
  width: fit-content;
  height: auto;
  border-radius: 5px;
}
/deep/.el-select__tags-text {
  display: inline;
  white-space: normal;
  word-break: break-word;
}

.el-input /deep/.el-input-group__prepend,
.el-input /deep/.el-input-group__append {
  background-color: #fff;
  padding: 0 10px;
}
// 太小会遮挡提示
/deep/ .el-form-item--mini.el-form-item,
.el-form-item--small.el-form-item {
  margin-bottom: 24px;
}

/deep/ .el-form-item__error {
  line-height: 20px;
}

/deep/ .el-checkbox,
/deep/ .el-radio {
  margin-right: 20px;
}
</style>
