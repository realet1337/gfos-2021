import Vue from 'vue'
import Vuex from 'vuex'

import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify';
import './assets/css/main.css'

Vue.config.productionTip = false

Vue.prototype.$apiBaseUrl = "http://192.168.178.102:8080"; //make this an empty string to point to same url
Vue.prototype.$messageChunkSize = 50;

Vue.use(Vuex);

const store = new Vuex.Store({
  state: {
    token: false,
    userId: false,
  },
  mutations: {
    setToken(state, token) {
      state.token = token;
    },
    setUserId(state, userId) {
      state.userId = userId;
    },
  }
});

new Vue({
  router,
  vuetify,
  store: store,
  render: h => h(App)
}).$mount('#app')
