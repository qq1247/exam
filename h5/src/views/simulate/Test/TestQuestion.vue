<template>
  <div class="content-center">
    <!--  -->
    <div v-if="!isRecite" class="question-content">
      <!-- 标题 -->
      <div v-if="questionDetail.type !== 3" class="question-title">
        <div>{{ questionDetail.id }}、</div>
        <div v-html="`${questionDetail.title}`" />
      </div>

      <div v-else class="question-title">
        <span>{{ questionDetail.id }}、</span>
        <ClozeTitle :detail="questionDetail" />
      </div>

      <!-- 单选 -->
      <el-radio-group
        v-if="questionDetail.type === 1"
        v-model="questionDetail.selected"
        class="children-option"
        @change="checkAnswer"
      >
        <el-radio
          v-for="(option, index) in questionDetail.options"
          :key="index"
          class="option-item"
          :disabled="questionDetail.finish ? true : false"
          :label="String.fromCharCode(65 + index)"
        >
          <div
            :style="{
              color: questionDetail.finish ? optionColor(index) : '',
            }"
            class="flex-items-center"
            v-html="`${String.fromCharCode(65 + index)}、${option}`"
          />
        </el-radio>
      </el-radio-group>

      <!-- 多选 -->
      <el-checkbox-group
        v-if="questionDetail.type === 2"
        v-model="questionDetail.selected"
        class="children-option"
        @change="checkAnswer"
      >
        <el-checkbox
          v-for="(option, index) in questionDetail.options"
          :key="index"
          class="option-item"
          :label="String.fromCharCode(65 + index)"
          :disabled="questionDetail.finish ? true : false"
        >
          <div
            :style="{
              color: questionDetail.finish ? optionColor(index) : '',
            }"
            class="flex-items-center"
            v-html="`${String.fromCharCode(65 + index)}、${option}`"
          />
        </el-checkbox>
      </el-checkbox-group>

      <!-- 判断 -->
      <el-radio-group
        v-if="questionDetail.type === 4"
        v-model="questionDetail.selected"
        class="children-option"
        @change="checkAnswer"
      >
        <el-radio
          v-for="(option, index) in ['对', '错']"
          :key="index"
          :label="option"
          class="option-item"
          :disabled="questionDetail.finish ? true : false"
        ><span
          :style="{
            color: questionDetail.finish ? optionColor(option) : '',
          }"
        >{{ option }}</span></el-radio>
      </el-radio-group>

      <!-- 问答 -->
      <template v-if="questionDetail.type === 5">
        <el-input
          v-model="questionDetail.selected"
          :rows="2"
          type="textarea"
          class="question-text"
          placeholder="请输入内容"
        />
      </template>
    </div>

    <div v-else class="question-content">
      <!-- 标题 -->
      <div v-if="questionDetail.type !== 3" class="question-title">
        <div>{{ questionDetail.id }}、</div>
        <div v-html="`${questionDetail.title}`" />
      </div>

      <div v-else class="question-title">
        <span>{{ questionDetail.id }}、</span>
        <ClozeTitle :detail="questionDetail" recite />
      </div>

      <!-- 单选 -->
      <el-radio-group
        v-if="questionDetail.type === 1"
        v-model="questionDetail.answers[0].answer"
        class="children-option"
      >
        <el-radio
          v-for="(option, index) in questionDetail.options"
          :key="index"
          class="option-item"
          :disabled="isRecite"
          :label="String.fromCharCode(65 + index)"
        >
          <div
            class="flex-items-center"
            v-html="`${String.fromCharCode(65 + index)}、${option}`"
          />
        </el-radio>
      </el-radio-group>

      <!-- 多选 -->
      <el-checkbox-group
        v-if="questionDetail.type === 2"
        v-model="questionDetail.answers[0].answer"
        class="children-option"
      >
        <el-checkbox
          v-for="(option, index) in questionDetail.options"
          :key="index"
          class="option-item"
          :label="String.fromCharCode(65 + index)"
          :disabled="isRecite"
        >
          <div
            class="flex-items-center"
            v-html="`${String.fromCharCode(65 + index)}、${option}`"
          />
        </el-checkbox>
      </el-checkbox-group>

      <!-- 判断 -->
      <el-radio-group
        v-if="questionDetail.type === 4"
        v-model="questionDetail.answers[0].answer"
        class="children-option"
      >
        <el-radio
          v-for="(option, index) in ['对', '错']"
          :key="index"
          :label="option"
          class="option-item"
          :disabled="isRecite"
        >{{ option }}</el-radio>
      </el-radio-group>

      <!-- 问答 -->
      <template v-if="questionDetail.type === 5">
        <template v-if="questionDetail.ai === 1">
          <div
            v-for="(answer, indexAnswers) in questionDetail.answers"
            :key="answer.id"
            class="answers-item"
          >
            <span>{{
              `关键词${$tools.intToChinese(indexAnswers + 1)}、`
            }}</span>
            <span
              v-for="(ans, indexAnswer) in answer.answer"
              :key="indexAnswer"
              class="answers-tag"
            >{{ ans }}</span>
          </div>
        </template>
        <div
          v-if="questionDetail.ai === 2"
          style="padding: 10px"
          v-html="`${questionDetail.answers[0].answer}`"
        />
      </template>
    </div>

    <el-row class="test-handler">
      <el-col :span="5">
        <el-button-group>
          <el-button
            size="small"
            type="primary"
            icon="el-icon-arrow-left"
            @click="prevQuestion"
          >上一题</el-button>
          <el-button
            size="small"
            type="primary"
            @click="nextQuestion"
          >下一题<i
            class="el-icon-arrow-right el-icon--right"
          /></el-button>
        </el-button-group>
      </el-col>
      <el-col
        :span="7"
      ><el-checkbox
         v-model="isRecite"
       ><div class="flex-items-center">背题模式</div>
       </el-checkbox>
        <el-checkbox
          v-model="isRandom"
          @change="(e) => $emit('randomTest', e)"
        ><div class="flex-items-center">随机模拟</div></el-checkbox></el-col>
      <el-col
        :span="5"
        :offset="7"
      ><el-button-group>
        <el-button
          size="small"
          type="primary"
          icon="el-icon-chat-line-round"
          @click="analysisDetail = !analysisDetail"
        >{{ analysisDetail ? '收起' : '显示' }}解析</el-button>
        <el-button
          size="small"
          type="primary"
          icon="el-icon-chat-dot-round"
          @click="showComment"
        >{{ commentDetail ? '收起' : '显示' }}评论</el-button>
      </el-button-group></el-col>
    </el-row>

    <div v-if="analysisDetail" class="question-analysis">
      【解析】：<span v-html="questionDetail.analysis" />
    </div>

    <CommentText
      v-if="commentDetail && commentState === 2"
      :key="questionDetail.id"
      @comment="commentReply"
    />
    <div class="question-comments">
      <QuestionComment
        v-if="commentDetail && commentList.length && commentState !== 0"
        :list="commentList"
        :comment-state="commentState"
        @showMore="showMore"
        @childrenComment="commentReply"
        @getChildrenComment="getChildrenComment"
      />
    </div>
  </div>
</template>

<script>
import { questionCommentAdd, questionCommentListPage } from 'api/question'
import ClozeTitle from './ClozeTitle.vue'
import QuestionComment from 'components/QuestionComment/Index.vue'
import CommentText from 'components/QuestionComment/CommentText.vue'
export default {
  components: {
    ClozeTitle,
    CommentText,
    QuestionComment
  },
  props: {
    questionDetail: {
      type: Object,
      default: () => {}
    },
    commentState: {
      type: Number,
      default: 0
    }
  },
  data() {
    return {
      curPage: 1,
      pageSize: 10,
      totalPage: 0,
      commentList: [],
      isRecite: false,
      isRandom: false,
      commentDetail: false,
      analysisDetail: false
    }
  },
  computed: {
    optionColor(index) {
      return (index) => {
        // 单选
        if (this.questionDetail.type === 1) {
          // 选择完毕且与正确答案不匹配
          if (
            this.questionDetail.selected === String.fromCharCode(65 + index) &&
            String.fromCharCode(65 + index) !==
              this.questionDetail.answers[0].answer
          ) {
            return '#FF5722'
          }

          // 正确答案
          if (
            this.questionDetail.answers[0].answer ===
            String.fromCharCode(65 + index)
          ) {
            return '#5FB878'
          }
        }

        // 判断
        if (this.questionDetail.type === 4) {
          // 选择完毕且与正确答案不匹配
          if (
            this.questionDetail.selected === index &&
            index !== this.questionDetail.answers[0].answer
          ) {
            return '#FF5722'
          }

          // 正确答案
          if (this.questionDetail.answers[0].answer === index) {
            return '#5FB878'
          }
        }

        // 多选
        if (this.questionDetail.type === 2) {
          // 选择完毕且与正确答案不匹配
          if (
            this.questionDetail.selected.includes(
              String.fromCharCode(65 + index)
            ) &&
            !this.questionDetail.answers[0].answer.includes(
              String.fromCharCode(65 + index)
            )
          ) {
            return '#FF5722'
          }

          // 正确答案
          if (
            this.questionDetail.answers[0].answer.includes(
              String.fromCharCode(65 + index)
            )
          ) {
            return '#5FB878'
          }
        }
      }
    }
  },
  methods: {
    // 校验答案
    checkAnswer() {
      let flag

      // 单选、判断
      if ([1, 4].includes(this.questionDetail.type)) {
        flag =
          this.questionDetail.selected === this.questionDetail.answers[0].answer
        this.questionDetail.finish = true
      }

      // 多选
      if (this.questionDetail.type === 2) {
        // 正确答案数组
        const answers = this.questionDetail.answers[0].answer.reduce(
          (acc, cur) => {
            acc.push(cur)
            return acc
          },
          []
        )
        // 所选项与正确答案是否一致
        if (this.questionDetail.selected.length !== answers.length) {
          const _flag = this.questionDetail.selected.some((item) =>
            answers.includes(item)
          )
          if (!_flag) {
            flag = false
            this.questionDetail.finish = true
          }
        } else {
          const _flag = this.questionDetail.selected.every((item) =>
            answers.includes(item)
          )
          flag = !!_flag
          this.questionDetail.finish = true
        }
      }

      if (flag) {
        setTimeout(() => {
          this.nextQuestion()
        }, 300)
      }
    },
    // 上一题
    prevQuestion() {
      this.$emit('prevQuestion')
    },
    // 下一题
    nextQuestion() {
      this.$emit('nextQuestion')
    },
    // 获取试题评论
    async questionComment(id) {
      const commentList = await questionCommentListPage({
        questionId: this.questionDetail.id,
        curPage: this.curPage,
        pageSize: this.pageSize,
        parentId: id || null
      })
      const totalPage =
        commentList.data.total % this.pageSize === 0
          ? commentList.data.total / this.pageSize
          : Math.ceil(commentList.data.total / this.pageSize)
      const moreComment = commentList.data.list.reduce((acc, cur) => {
        const params = {
          id: cur.id,
          curPage: 1,
          content: cur.content,
          time: cur.createTime,
          name: cur.createUserName || '匿名用户',
          avatar: cur.createUserHeadFileId
        }
        acc.push(id ? params : { ...params, replay: false, children: [] })
        return acc
      }, [])
      return { totalPage, moreComment }
    },
    // 试题一级评论
    async getQuestionComment() {
      const { totalPage, moreComment } = await this.questionComment()
      this.totalPage = totalPage
      this.commentList = [...this.commentList, ...moreComment]
    },
    // 获取二级回复列表
    async getChildrenComment(id) {
      const { totalPage, moreComment } = await this.questionComment(id)
      if (!moreComment.length) {
        this.$message({
          message: '没有更多了',
          duration: 1000
        })
        return
      }
      this.commentList.filter((item, index) => {
        if (item.id === id) {
          this.$set(this.commentList[index], 'totalPage', totalPage)
          this.commentList[index].children = [
            ...this.commentList[index].children,
            ...moreComment
          ]
          return true
        }
      })
    },
    // 评论回复
    async commentReply(text, anonymity, id) {
      if (!text.trim()) {
        this.$message.warning('请填写评论内容！')
        return
      }

      const res = await questionCommentAdd({
        questionId: this.questionDetail.id,
        content: text,
        anonymity: anonymity ? 0 : 1,
        parentId: id || null
      })

      if (res?.code === 200) {
        this.commentList = []
        await this.getQuestionComment()
        id && this.getChildrenComment(id)
      }
    },
    // 查看更多一级回复
    async showMore(id, index) {
      if (index) {
        // 二级回复查看更多
        if (
          this.commentList[index].curPage === this.commentList[index].totalPage
        ) {
          this.$message('没有更多了')
          return
        }
        this.commentList[index].curPage += 1
        const { totalPage, moreComment } = await this.questionComment(id)
        this.$set(this.commentList[index], 'totalPage', totalPage)
        this.commentList[index].children = [
          ...this.commentList[index].children,
          ...moreComment
        ]
      } else {
        // 一级回复查看更多
        if (this.curPage === this.totalPage) {
          this.$message('没有更多了')
          return
        }
        this.curPage += 1
        const { totalPage, moreComment } = await this.questionComment()
        this.totalPage = totalPage
        this.commentList = [...this.commentList, ...moreComment]
      }
    },
    // 展示评论
    showComment() {
      this.commentDetail = !this.commentDetail
      if (this.commentDetail) {
        this.commentList = []
        this.getQuestionComment()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import 'assets/style/exam.scss';
.content-center {
  margin-left: 220px;
  padding: 0;
}
.question-content {
  min-height: 400px;
  height: auto;
}
.test-handler {
  padding: 16px;
}
.question-analysis {
  display: flex;
  padding: 10px;
  background: #fff;
  border-bottom: 10px solid #f7f8f9;
}

.answers-item {
  line-height: 40px;
  margin-left: 40px;
}
</style>
