/*
 * @Description: 我的API
 * @Version: 1.0
 * @Company:
 * @Author: Che
 * @Date: 2021-08-11 17:06:11
 * @LastEditors: Che
 * @LastEditTime: 2021-12-03 14:06:45
 */
import request from './request'

export const myExamListPage = (params) => request('myExam/listpage', params)
export const myExamAnswerList = (params) => request('myExam/answerList', params)
export const myExamAnswer = (params) => request('myExam/answer', params)
export const myExamFinish = (params) => request('myExam/finish', params)

export const myMarkListPage = (params) => request('myMark/listpage', params)
export const myMarkExamListPage = (params) =>
  request('myMark/examListpage', params)
export const myMarkScore = (params) => request('myMark/score', params)
export const myMarkFinish = (params) => request('myMark/finish', params)

export const myMarkAnswerList = (params) => request('myMark/answerList', params)
export const myMarkUserList = (params) => request('myMark/userList', params)
export const myMarkUser = (params) => request('myMark/user', params)
