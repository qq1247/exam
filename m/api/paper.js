import qs from 'qs'

const http = uni.$u.http

// 获取试卷信息
export const paperGet = (params, config = {custom: { auth: true }}) => http.post('paper/get', params, config)

// 获取试卷试题（人工）
export const paperPaper = (params, config = {custom: { auth: true }}) => http.post('paper/paper', params, config)

// 获取个人的试卷试题
export const myExamPaper = (params, config = {custom: { auth: true }}) => http.post('myExam/paper', params, config)