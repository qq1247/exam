import request from './request'

/**
 * @name: login
 * @description: 登录
 * @param {*}
 * @return { accessToken, userId, userName, roles }
 */
export const login = (params) => request('login/in', params)

/**
 * @name: loginPwd
 * @description: 设置密码
 * @param {*}
 * @return {*}
 */
export const loginPwd = (params) => request('login/pwd', params)

/**
 * @name: loginSysTime
 * @description: 系统时间
 * @param {*}
 * @return {*}
 */
export const loginSysTime = (params) => request('login/sysTime', params)

/**
 * @name: loginEntName
 * @description: 获取企业name
 * @param {*}
 * @return {*}
 */
export const loginEntName = (params) => request('login/entName', params)

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

/**
 * @name: progressBarGet
 * @description: 获取进度
 * @param {*}
 * @return {*}
 */
export const progressBarGet = (params) => request('progressBar/get', params)
