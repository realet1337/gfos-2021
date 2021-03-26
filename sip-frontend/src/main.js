import Vue from 'vue'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify';

Vue.config.productionTip = false

Vue.prototype.$apiBaseUrl = "http://192.168.178.101:8080"; //make this an empty string to point to same url

new Vue({
  router,
  vuetify,
  render: h => h(App)
}).$mount('#app')
