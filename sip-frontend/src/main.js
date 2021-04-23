import Vue from 'vue'

import store from '@/store/'

import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify';
import './assets/css/main.css'

//global event bus
Vue.prototype.$eventHub = new Vue();

Vue.prototype.$apiHttpUrl = "http://192.168.178.35:8080"; //make this an empty string to point to same url
Vue.prototype.$apiWsUrl = "ws://192.168.178.35:8080"; //make this an empty string to point to same url
Vue.prototype.$notificationTimeout = 5000;

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
			if(!store.state.blockedUsers.some(user => user.id == chatMessage.author.id)
			&& (!store.state.blockedBy.some(user => user.id == chatMessage.author.id)
				|| store.state.userProfile.reverseBlocking === 0
				)
			){
				Vue.prototype.$eventHub.$emit('new-message', chatMessage);
			}
		}
		else if(message.data.startsWith('blocked: ')){
			var blockedUser = JSON.parse(message.data.substring(9));
			store.commit('setBlockedUsers', store.state.blockedUsers.concat([blockedUser]));
		}
		else if(message.data.startsWith('unblocked: ')){
			var unblockedUser = JSON.parse(message.data.substring(11));
			var userIndex = store.state.blockedUsers.findIndex(user => unblockedUser.id == user.id);
			//remove from list without having to implement store properly
			store.commit('setBlockedUsers', store.state.blockedUsers.slice(0, userIndex).concat(store.state.blockedUsers.slice(userIndex + 1)));
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
	var todo = 4;

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

	window.axios.get(Vue.prototype.$apiHttpUrl + '/api/users/' + store.state.userId).then((response) => {
		store.commit('setUser', response.data);
		todo--;
		if(todo == 0){
			store.commit('setInitialized', true);
		}
	}, ()=>{
		//same thing here
		if(!abort){
			abort = true;
			store.commit('reset');
			router.push('/');
		}
	});

	window.axios.get(Vue.prototype.$apiHttpUrl + '/api/users/' + store.state.userId + '/profile',{
		headers:{
				'Authorization': 'Bearer ' + store.state.token,
		}
	}).then((response) => {
		store.commit('setUserProfile', response.data);
		todo--;
		if(todo == 0){
			store.commit('setInitialized', true);
		}
	}, ()=>{
		//same thing here
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
