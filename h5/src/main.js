import Vue from "vue"
import App from "./App.vue"
import router from "./router"
import store from "./store"

import "@/assets/css/normalize.css"
import ElementUI from "element-ui"
import "element-ui/lib/theme-chalk/index.css"
import "@/assets/css/element-ui-ex.scss"
import api from "@/api"
Vue.use(ElementUI)

Vue.prototype.$https = api
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app")
