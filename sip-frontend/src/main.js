import Vue from 'vue'

import store from '@/store/'

import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify';
import './assets/css/main.css'

//global event bus
Vue.prototype.$eventHub = new Vue();

Vue.config.productionTip = false;

Vue.prototype.$apiHttpUrl = "http://192.168.178.103:8080"; //make this an empty string to point to same url
Vue.prototype.$apiWsUrl = "ws://192.168.178.103:8080"; //make this an empty string to point to same url
Vue.prototype.$messageChunkSize = 50;
Vue.prototype.$maxLoadedMessages = 100; //these are pretty safe, there's some room here
Vue.prototype.$notificationTimeout = 500000;

//THE VOID
Vue.prototype.$void = [
  "There's nothing here...", 
  "Everything is silent. Too silent.", 
  "This is the most boring place ever.",
  "This place is dangerous for people like you.",
  "Are you looking for something?",
  "Ah, you're finally awake.",
  "Where did they all go?"
]

Vue.prototype.$getAvatarUrl = function (type, obj){
  if(type === "user"){
      return Vue.prototype.$apiHttpUrl + "/upload/pic/user/" + obj.profilePicture + ".jpg";
  }else if(type==="group") {
      return Vue.prototype.$apiHttpUrl + "/upload/pic/group/" + obj.picture + ".jpg";
  }
  return;
  
}

function createUserWatcher(){
  var ws = new WebSocket(Vue.prototype.$apiWsUrl + '/api/users/' +  store.state.userId + '/websockets');
  ws.onopen = function(){
    ws.send('Bearer ' + store.state.token);
  }
  ws.onmessage = function(message){

    if(message.data.startsWith('new-message: ')){
      var chatMessage = JSON.parse(message.data.substring(13));
      if(store.state.blockedUsers.findIndex(user => user.id == chatMessage.author.id) === -1
      && (store.state.blockedBy.findIndex(user => user.id == chatMessage.author.id) === -1
        || store.state.userProfile.reverseBlocking === 0
        )
      ){
        Vue.prototype.$eventHub.$emit('new-message', chatMessage);
      }
    }
  }
  ws.onerror = function(){
    setTimeout(createUserWatcher, 10000);
  }
  store.commit('setWs', ws);
}

Vue.prototype.$initApp = function(){

  createUserWatcher();

  var abort = false;
  var todo = 2;

  window.axios.get(Vue.prototype.$apiHttpUrl + '/api/users/' + store.state.userId + '/blocked-users', {
    headers:{
        'Authorization': 'Bearer ' + store.state.token,
    }
  }).then((response) => {
    store.commit('setBlockedUsers', response.data);
    todo--;
    if(todo == 0){
      store.commit('setInitialized', true);
    }
  }, () => {
      //this shouldn't fail so we'll just perform logout
      if(!abort){
        abort = true;
        store.commit('reset');
        router.push('/');
      }
  });
  window.axios.get(Vue.prototype.$apiHttpUrl + '/api/users/' + store.state.userId + '/blocked-by', {
    headers:{
        'Authorization': 'Bearer ' + store.state.token,
    }
  }).then((response) => {
    store.commit('setBlockedBy', response.data);
    todo--;
    if(todo == 0){
      store.commit('setInitialized', true);
    }
  }, () => {
      //this shouldn't fail either
      if(!abort){
        abort = true;
        store.commit('reset');
        router.push('/');
      }
  });
}


new Vue({
  router,
  vuetify,
  store: store,
  render: h => h(App)
}).$mount('#app')
