import Vue from 'vue'
import VueRouter from 'vue-router'
import { Message } from 'element-ui'
import Home from '../views/Home'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/base/Login.vue')
  },
  // 试卷相关
  {
    path: '/examPaper',
    name: 'ExamPaper',
    component: () => import('../views/examPaper/Index.vue')
  },
  {
    path: '/examPaper/list',
    name: 'ExamPaperList',
    component: () => import('../views/examPaper/List.vue')
  },
  {
    path: '/examPaper/edit',
    name: 'PaperEdit',
    component: () => import('../views/examPaper/Edit.vue')
  },
  // 考试相关
  {
    path: '/examSetting',
    name: 'ExamSetting',
    component: () => import('../views/examSetting/Index.vue')
  },
  {
    path: '/examSetting/list',
    name: 'SettingList',
    component: () => import('../views/examSetting/List.vue')
  },
  // 试题相关
  {
    path: '/examLibrary',
    name: 'ExamLibrary',
    component: () => import('../views/examLibrary/Index.vue')
  },
  {
    path: '/examLibrary/edit',
    name: 'LibraryEdit',
    component: () => import('../views/examLibrary/Edit.vue')
  },
  // 组织机构
  {
    path: '/organization/cron',
    name: 'Cron',
    component: () => import('../views/organization/Cron.vue')
  },
  {
    path: '/organization/user',
    name: 'User',
    component: () => import('../views/organization/User.vue')
  },
  {
    path: '/organization/org',
    name: 'Org',
    component: () => import('../views/organization/Org.vue')
  },
  {
    path: '/organization/parm',
    name: 'Parm',
    component: () => import('../views/organization/Parm.vue')
  },
  {
    path: '/organization/dict',
    name: 'Dict',
    component: () => import('../views/organization/Dict.vue')
  },
  {
    path: '/organization/bulletinBoard',
    name: 'BulletinBoard',
    component: () => import('../views/organization/BulletinBoard.vue')
  },
  {
    path: '/organization/myExam',
    name: 'OrgMyExam',
    component: () => import('../views/organization/MyExam.vue')
  },
  {
    path: '/organization/myMark',
    name: 'MyMark',
    component: () => import('../views/organization/MyMark.vue')
  },
  {
    path: '/organization/myMarkExam',
    name: 'MyMarkExam',
    component: () => import('../views/organization/MyMarkExam.vue')
  },
  // 我的
  {
    path: '/my',
    name: 'My',
    component: () => import('../views/my/index.vue')
  },
  {
    path: '/my/exam',
    name: 'MyExam',
    component: () => import('../views/my/MyExam.vue')
  },
  {
    path: '/my/checkPaper',
    name: 'MyCheckPaper',
    component: () => import('../views/my/MyCheckPaper.vue')
  },
  { path: '*', component: () => import('../views/base/404.vue') }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 使用 router.beforeEach 注册一个全局前置守卫，判断用户是否登陆
router.beforeEach((to, from, next) => {
  if (to.path != '/login') {
    const token = localStorage.getItem('token')
    if (!token) {
      Message({
        message: '请您重新登录',
        duration: 2000,
        type: 'warning'
      })
      next({
        path: '/login',
        query: {
          redirect: to.fullPath
        }
      })
    }
  }
  next()
})

export default router
