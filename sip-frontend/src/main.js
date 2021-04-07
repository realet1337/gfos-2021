import Vue from 'vue'

import store from '@/store/'

import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify';
import './assets/css/main.css'

Vue.config.productionTip = false

Vue.prototype.$apiHttpUrl = "http://192.168.178.103:8080"; //make this an empty string to point to same url
Vue.prototype.$apiWsUrl = "ws://192.168.178.103:8080"; //make this an empty string to point to same url
Vue.prototype.$messageChunkSize = 50;
Vue.prototype.$getAvatarUrl = function (type, obj){
  if(type === "user"){
      return Vue.prototype.$apiHttpUrl + "/upload/pic/user/" + obj.profilePicture + ".jpg";
  }else if(type==="group") {
      return Vue.prototype.$apiHttpUrl + "/upload/pic/group/" + obj.picture + ".jpg";
  }
  return;
  
}


new Vue({
  router,
  vuetify,
  store: store,
  render: h => h(App)
}).$mount('#app')
