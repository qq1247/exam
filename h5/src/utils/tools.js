/*
 * @Description:
 * @Version: 1.0
 * @Company:
 * @Author: Che
 * @Date: 2021-07-27 17:31:01
 * @LastEditors: Che
 * @LastEditTime: 2021-10-19 09:35:08
 */
const resetData = (el, name) => {
  const $data = el.$data[name]
  const data = el.$options.data()[name]
  for (const attrName in $data) {
    if (attrName !== 'rules') {
      $data[attrName] = data[attrName]
    }
  }
}

/**
 * @name: delay
 * @description: 异步倒计时
 * @param { msec } 秒数
 * @return {*}
 */
const delay = (msec = 1) => {
  return new Promise((resolve) => {
    setTimeout(() => resolve(), msec * 1000)
  })
}

/**
 * @name: getQueryParam
 * @description: 获取url指定参数
 * @param { url, name } url地址，参数名称
 * @return {*}
 */
const getQueryParam = (url, name) => {
  var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i')
  var r = url.slice(url.indexOf('?') + 1).match(reg)
  if (r != null) return decodeURI(r[2])
  return null
}

/**
 * @name: formateTime
 * @description: 格式化时间
 * @param { time } 毫秒数
 * @return { hours, minutes, seconds }  时分秒
 */
const formateTime = (time) => {
  const SECOND = 1000
  const MINUTE = 60 * SECOND
  const HOUR = 60 * MINUTE
  const hours = Math.floor(time / HOUR)
  const minutes = Math.floor((time % HOUR) / MINUTE)
  const seconds = Math.floor((time % MINUTE) / SECOND)

  return {
    hours,
    minutes,
    seconds,
  }
}

/**
 * @name: intToChinese
 * @description: 数字转换成汉字
 * @param { value } 数字
 * @return {*}
 */
const intToChinese = (value) => {
  const str = String(value)
  const len = str.length - 1
  const digits = [
    '',
    '十',
    '百',
    '千',
    '万',
    '十',
    '百',
    '千',
    '亿',
    '十',
    '百',
    '千',
    '万',
    '十',
    '百',
    '千',
    '亿',
  ]
  const num = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九']
  return str.replace(/([1-9]|0+)/g, ($, $1, idx, full) => {
    let pos = 0
    if ($1[0] !== '0') {
      pos = len - idx
      if (idx == 0 && $1[0] == 1 && digits[len - idx] == '十') {
        return digits[len - idx]
      }
      return num[$1[0]] + digits[len - idx]
    } else {
      let left = len - idx
      let right = len - idx + $1.length
      if (Math.floor(right / 4) - Math.floor(left / 4) > 0) {
        pos = left - (left % 4)
      }
      if (pos) {
        return digits[pos] + num[$1[0]]
      } else if (idx + $1.length >= len) {
        return ''
      } else {
        return num[$1[0]]
      }
    }
  })
}

export { resetData, delay, getQueryParam, formateTime, intToChinese }
