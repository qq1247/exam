/*
 * @Description: 公共API
 * @Version: 1.0
 * @Company:
 * @Author: Che
 * @Date: 2021-08-11 13:25:40
 * @LastEditors: Che
 * @LastEditTime: 2021-08-11 22:52:22
 */
import request from './request'

/**
 * @name: login
 * @description: 登录
 * @param {*}
 * @return { accessToken, userId, userName, roles }
 */
export const login = (params) => request('login/in', params)

/**
 * @name: loginPwdUpdate
 * @description: 设置密码
 * @param {*}
 * @return {*}
 */
export const loginPwdUpdate = (params) => request('login/pwdUpdate', params)

/**
 * @name: loginSysTime
 * @description: 系统时间
 * @param {*}
 * @return {*}
 */
export const loginSysTime = (params) => request('login/sysTime', params)

/**
 * @name: fileUpload
 * @description: 文件上传
 * @param {*}
 * @return {*}
 */
export const fileUpload = (params) => request('file/upload', params)

/**
 * @name: fileDownload
 * @description: 文件下载
 * @param {*}
 * @return {*}
 */
export const fileDownload = (params) => request('file/download', params)
