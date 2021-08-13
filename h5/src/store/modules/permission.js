/*
 * @Description:
 * @Version: 1.0
 * @Company:
 * @Author: Che
 * @Date: 2021-08-11 11:33:30
 * @LastEditors: Che
 * @LastEditTime: 2021-08-13 16:29:02
 */
import { asyncRoutes, constantRoutes } from '@/router/index'
import {
  getAsyncRoutes,
  setAsyncRoutes,
  getFinallyRoutes,
  setFinallyRoutes,
} from '@/utils/storage'

/**
 * @name: hasPermission
 * @description: 是否具有权限
 * @param {*} roles
 * @param {*} route
 * @return {*}
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some((role) => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * @name: filterAsyncRoutes
 * @description: 过滤权限
 * @param {*} routes
 * @param {*} roles
 * @return {*}
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach((route) => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: getAsyncRoutes(),
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
    setAsyncRoutes(routes)
  },
}

const actions = {
  generateRoutes({ commit, state }, roles) {
    return new Promise((resolve) => {
      let accessedRoutes
      if (roles.includes('admin')) {
        accessedRoutes = asyncRoutes || []
      } else {
        accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      }
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })
  },
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
}
