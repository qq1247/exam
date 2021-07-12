const questionList = {
  code: 200,
  msg: '请求成功',
  data: {
    total: 7,
    list: [
      {
        no: 1,
        ver: 1,
        updateUserId: 1,
        updateUserName: '管理员',
        typeName: '单选',
        scoreOptions: null,
        updateTime: '2021-06-11T06:44:06.000+0000',
        analysis: '2',
        questionTypeId: 13,
        title: '1+1=?',
        type: 1,
        difficultyName: '极易',
        difficulty: 1,
        questionTypeName: '原子弹',
        score: 1,
        answer: 'C',
        srcId: 4,
        state: 1,
        id: 4,
        option: [
          {
            id: 12,
            options: '<p>0</p>',
            no: 1,
            questionId: 4
          },
          {
            id: 13,
            options: '<p>1</p>',
            no: 2,
            questionId: 4
          },
          {
            id: 14,
            options: '<p>2</p>',
            no: 3,
            questionId: 4
          }
        ]
      },
      {
        no: 1,
        ver: 1,
        updateUserId: 1,
        updateUserName: '管理员',
        typeName: '单选',
        scoreOptions: null,
        updateTime: '2021-06-18T06:43:33.000+0000',
        analysis: '2',
        questionTypeId: 13,
        title: '1+1=?',
        type: 1,
        difficultyName: '极易',
        difficulty: 1,
        questionTypeName: '原子弹',
        score: 1,
        answer: 'C',
        srcId: 4,
        state: 1,
        id: 5,
        option: []
      },
      {
        no: 1,
        ver: 1,
        updateUserId: 1,
        updateUserName: '管理员',
        typeName: '单选',
        scoreOptions: null,
        updateTime: '2021-06-18T06:44:21.000+0000',
        analysis: '77777',
        questionTypeId: 20,
        title: '12345678977777',
        type: 1,
        difficultyName: '适中',
        difficulty: 3,
        questionTypeName: '86878',
        score: 1,
        answer: 'B',
        srcId: 6,
        state: 2,
        id: 6,
        option: [
          {
            id: 15,
            options: '<p>777</p>',
            no: 1,
            questionId: 6
          },
          {
            id: 16,
            options: '<p>77</p>',
            no: 2,
            questionId: 6
          },
          {
            id: 17,
            options: '<p>77</p>',
            no: 3,
            questionId: 6
          }
        ]
      },
      {
        no: 1,
        ver: 1,
        updateUserId: 1,
        updateUserName: '管理员',
        typeName: '多选',
        scoreOptions: '1',
        updateTime: '2021-06-18T10:04:50.000+0000',
        analysis: '123',
        questionTypeId: 20,
        title: 'ff',
        type: 2,
        difficultyName: '极易',
        difficulty: 1,
        questionTypeName: '86878',
        score: 1,
        answer: 'A,B,C',
        srcId: 7,
        state: 2,
        id: 7,
        option: [
          {
            id: 18,
            options: '<p>1</p>',
            no: 1,
            questionId: 7
          },
          {
            id: 19,
            options: '<p>2</p>',
            no: 2,
            questionId: 7
          },
          {
            id: 20,
            options: '<p>3</p>',
            no: 3,
            questionId: 7
          }
        ]
      },
      {
        no: 1,
        ver: 1,
        updateUserId: 1,
        updateUserName: '管理员',
        typeName: '填空',
        scoreOptions: '1,4',
        updateTime: '2021-06-21T01:33:59.000+0000',
        analysis: '嘎嘎嘎',
        questionTypeId: 20,
        title: '1111',
        type: 3,
        difficultyName: '极易',
        difficulty: 1,
        questionTypeName: '86878',
        score: 1,
        answer: '反对法\nfa方大',
        srcId: 8,
        state: 2,
        id: 8
      },
      {
        no: 1,
        ver: 1,
        updateUserId: 1,
        updateUserName: '管理员',
        typeName: '判断',
        scoreOptions: null,
        updateTime: '2021-06-21T01:34:18.000+0000',
        analysis: '999',
        questionTypeId: 20,
        title: '9999',
        type: 4,
        difficultyName: '极易',
        difficulty: 1,
        questionTypeName: '86878',
        score: 1,
        answer: '对',
        srcId: 9,
        state: 2,
        id: 9
      },
      {
        no: 1,
        ver: 1,
        updateUserId: 1,
        updateUserName: '管理员',
        typeName: '问答',
        scoreOptions: null,
        updateTime: '2021-06-21T01:34:28.000+0000',
        analysis: '888',
        questionTypeId: 20,
        title: '888',
        type: 5,
        difficultyName: '极易',
        difficulty: 1,
        questionTypeName: '86878',
        score: 1,
        answer: '<p>888</p>',
        srcId: 10,
        state: 2,
        id: 10
      }
    ]
  }
}

// 难度
const difficulty = [
  {
    dictValue: '极易',
    no: 1,
    id: 13,
    dictKey: '1',
    dictIndex: 'QUESTION_DIFFICULTY'
  },
  {
    dictValue: '简单',
    no: 2,
    id: 14,
    dictKey: '2',
    dictIndex: 'QUESTION_DIFFICULTY'
  },
  {
    dictValue: '适中',
    no: 3,
    id: 15,
    dictKey: '3',
    dictIndex: 'QUESTION_DIFFICULTY'
  },
  {
    dictValue: '困难',
    no: 4,
    id: 16,
    dictKey: '4',
    dictIndex: 'QUESTION_DIFFICULTY'
  },
  {
    dictValue: '极难',
    no: 5,
    id: 17,
    dictKey: '5',
    dictIndex: 'QUESTION_DIFFICULTY'
  }
]

// 类型
const questionType = [
  { dictValue: '单选', no: 1, id: 8, dictKey: '1', dictIndex: 'QUESTION_TYPE' },
  { dictValue: '多选', no: 2, id: 9, dictKey: '2', dictIndex: 'QUESTION_TYPE' },
  {
    dictValue: '填空',
    no: 3,
    id: 10,
    dictKey: '3',
    dictIndex: 'QUESTION_TYPE'
  },
  {
    dictValue: '判断',
    no: 4,
    id: 11,
    dictKey: '4',
    dictIndex: 'QUESTION_TYPE'
  },
  { dictValue: '问答', no: 5, id: 12, dictKey: '5', dictIndex: 'QUESTION_TYPE' }
]

const cardList = {
  code: 200,
  msg: '请求成功',
  data: {
    total: 1,
    list: [
      {
        createUserId: 1,
        rwState: 2,
        imgId: null,
        updateUserId: 1,
        createTime: '2017-08-01T14:31:43.000+0000',
        name: '试题分类',
        writeUserIds: null,
        updateTime: '2017-08-01T14:31:43.000+0000',
        createUserName: '管理员',
        state: 1,
        id: 1,
        readUserIds: null
      }
    ]
  }
}

// 试卷试题
const paperList = {
  code: 200,
  msg: '请求成功',
  data: [
    {
      chapter: {
        name: '单选题',
        description: '每题2分，一共1道，总2分',
        id: 1,
        parentId: 1
      },
      questionList: [
        {
          updateUserName: '张虎成',
          typeName: '单选',
          answers: ['D'],
          scoreOptions: null,
          type: 1,
          title: '<p><strong>下列哪一种叙述是正确的</strong></p>',
          analysis:
            '<p>abstract（抽象）修饰符，可以修饰类和方法<br>abstract修饰符在修饰类时必须放在类名前。<br>public abstract void method();</p>',
          difficultyName: '极易',
          difficulty: 1,
          paperQuestionId: 6,
          score: 1,
          options: [
            '<p>abstract修饰符可修饰字段、方法和类</p>',
            '<p>抽象方法的body部分必须用一对大括号{ }包住</p>',
            '<p>声明抽象方法，大括号可有可无</p>',
            '<p>声明抽象方法不可写出大括号</p>'
          ],
          id: 1
        }
      ]
    },
    {
      chapter: {
        name: '多选题',
        description: '每题2分，一共1道，总2分',
        id: 2,
        parentId: 1
      },
      questionList: [
        {
          updateUserName: '张虎成',
          typeName: '多选',
          answers: ['A', 'D'],
          scoreOptions: null,
          type: 2,
          title: '<p><strong>下列属于jsp中注释的有</strong></p>',
          analysis: '<p>无</p>',
          difficultyName: '极易',
          difficulty: 1,
          paperQuestionId: 7,
          score: 1,
          options: [
            '<p>&lt;%-- &nbsp;与 --%&gt;</p>',
            '<p>/</p>',
            '<p>/** 与 **/</p>',
            '<p>&lt;!-- 与 --&gt;</p>'
          ],
          id: 2
        }
      ]
    },
    {
      chapter: {
        name: '选答题',
        description: '每题2分，一共1道，总2分',
        id: 20,
        parentId: 1
      },
      questionList: []
    },
    {
      chapter: {
        name: '填空题',
        description: '每题2分，一共1道，总2分',
        id: 3,
        parentId: 1
      },
      questionList: [
        {
          updateUserName: '张虎成',
          typeName: '填空',
          answers: ['临时态', '持久态', '游离态'],
          scoreOptions: null,
          type: 3,
          title:
            '<p><i>hibernate的三种状态分别是_____</i>__、<i>_____</i>__、<i>_____</i>__</p>',
          analysis: '',
          difficultyName: '极易',
          difficulty: 1,
          paperQuestionId: 8,
          score: 1,
          options: [],
          id: 3
        }
      ]
    },
    {
      chapter: {
        name: '组合题',
        description: '每题2分，一共1道，总2分',
        id: 10,
        parentId: 1
      },
      questionList: []
    },
    {
      chapter: {
        name: '选择题',
        description: '每题2分，一共1道，总2分',
        id: 4,
        parentId: 1
      },
      questionList: [
        {
          updateUserName: '张虎成',
          typeName: '判断',
          answers: ['错'],
          scoreOptions: null,
          type: 4,
          title: '<p>在Java的方法中定义一个常量要用const关键字。</p>',
          analysis: '<p>在java中定义常量用final。</p>',
          difficultyName: '困难',
          difficulty: 4,
          paperQuestionId: 9,
          score: 1,
          options: [],
          id: 4
        }
      ]
    },
    {
      chapter: {
        name: '问答题',
        description: '每题2分，一共1道，总2分',
        id: 5,
        parentId: 1
      },
      questionList: []
    }
  ]
}

// 试卷信息
const paperIntro = {
  code: 200,
  msg: '请求成功',
  data: {
    id: 1,
    name: 'java内部测试',
    passScore: 60,
    totalScore: 4,
    showType: 1,
    readRemark: '&lt;p&gt;注意事项：&lt;/p&gt;',
    readNum: 3,
    state: 2,
    paperTypeId: 2,
    genType: 0,
    options: '',
    minimizeNum: null,
    updateUserId: 2,
    updateTime: '2021-07-02 11:12:03'
  }
}

// 试卷答案
const answerList = {
  code: 200,
  msg: '请求成功',
  data: [
    {
      questionId: 1,
      answerTime: '2021-07-02T03:22:16.000+0000',
      answers: ['A'],
      questionScore: 1,
      markTime: null,
      markUserName: null,
      userId: 2,
      score: null,
      markUserId: null,
      examId: 1,
      questionType: 1,
      myExamDetailId: 1,
      myExamId: 1
    },
    {
      questionId: 2,
      answerTime: '2021-07-02T03:22:28.000+0000',
      answers: ['A', 'D'],
      questionScore: 1,
      markTime: null,
      markUserName: null,
      userId: 2,
      score: null,
      markUserId: null,
      examId: 1,
      questionType: 2,
      myExamDetailId: 2,
      myExamId: 1
    },
    {
      questionId: 3,
      answerTime: '2021-07-02T03:25:00.000+0000',
      answers: ['请求', '问问', '嗯嗯'],
      questionScore: 1,
      markTime: null,
      markUserName: null,
      userId: 2,
      score: null,
      markUserId: null,
      examId: 1,
      questionType: 3,
      myExamDetailId: 3,
      myExamId: 1
    },
    {
      questionId: 4,
      answerTime: '2021-07-02T03:22:33.000+0000',
      answers: ['对'],
      questionScore: 1,
      markTime: null,
      markUserName: null,
      userId: 2,
      score: null,
      markUserId: null,
      examId: 1,
      questionType: 4,
      myExamDetailId: 4,
      myExamId: 1
    }
  ]
}

// 编辑试题
const editQuestionList = [
  {
    difficulty: 1,
    score: 1,
    no: 1,
    options: [
      '<p>abstract修饰符可修饰字段、方法和类</p>',
      '<p>抽象方法的body部分必须用一对大括号{ }包住</p>',
      '<p>声明抽象方法，大括号可有可无</p>',
      '<p>声明抽象方法不可写出大括号</p>'
    ],
    answers: ['D'],
    scoreOptions: null,
    id: 1,
    state: 2,
    type: 1,
    title: '<p><strong>下列哪一种叙述是正确的</strong></p>',
    analysis:
      '<p>abstract（抽象）修饰符，可以修饰类和方法<br>abstract修饰符在修饰类时必须放在类名前。<br>public abstract void method();</p>',
    questionTypeId: 3
  },
  {
    difficulty: 1,
    score: 1,
    no: 1,
    options: [
      '<p>&lt;%-- &nbsp;与 --%&gt;</p>',
      '<p>/</p>',
      '<p>/** 与 **/</p>',
      '<p>&lt;!-- 与 --&gt;</p>'
    ],
    answers: ['A', 'D'],
    scoreOptions: null,
    id: 2,
    state: 2,
    type: 2,
    title: '<p><strong>下列属于jsp中注释的有</strong></p>',
    analysis: '<p>无</p>',
    questionTypeId: 3
  },
  {
    difficulty: 1,
    score: 2,
    no: 1,
    options: [],
    answers: ['封装', '多态', '继承'],
    scoreOptions: '1',
    id: 3,
    state: 2,
    type: 3,
    title: '<p><i>Java的特性分别是_____</i>、<i>_____</i>、<i>_____</i></p>',
    analysis: '',
    questionTypeId: 3
  },
  {
    difficulty: 4,
    score: 1,
    no: 1,
    options: [],
    answers: ['错'],
    scoreOptions: null,
    id: 4,
    state: 2,
    type: 4,
    title: '<p>在Java的方法中定义一个常量要用const关键字。</p>',
    analysis: '<p>在java中定义常量用final。</p>',
    questionTypeId: 3
  },
  {
    difficulty: 5,
    score: 1,
    no: 1,
    options: [],
    answers: [
      '<p>string是final的，内部用一个final类型的char数组存储数据，它的拼接效率比较低，实际上是通过建立一个StringBuffer'
    ],
    scoreOptions: null,
    id: 5,
    state: 2,
    type: 5,
    title: '<p>简述string对象，StringBuffer、StringBuilder区分</p>',
    analysis:
      '<p>StringBuffer也是final,线程安全的，中采用一个char数组来保存需要append的字符串，char数组有一个初始大小，当append的字符串长度超过当前char数组容量时，则对char数组进行动态扩展，也即重新申请一段更大的内存空间，然后将当前char数组拷贝到新的位置，因为重新分配内存并拷贝的开销比较大，所以每次重新申请内存空间都是采用申请大于当前需要的内存空间的方式，这里是2倍。</p>',
    questionTypeId: 3
  }
]

export {
  questionList,
  cardList,
  paperList,
  paperIntro,
  answerList,
  difficulty,
  questionType,
  editQuestionList
}
