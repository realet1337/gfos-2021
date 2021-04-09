import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store/'

import Login from '@/views/Login.vue'
import Home from '@/views/Home.vue'
import FrontPage from '@/views/FrontPage.vue'
import DirectChat from '@/views/DirectChat.vue'

import HomeDirectChats from '@/components/HomeDirectChats.vue'
import HomeGroups from '@/components/HomeGroups.vue'


Vue.use(VueRouter)

const routes = [
	{
		path: '/login',
		name: 'Login',
		component: Login
	},
	{
		path: '/home',
		component: Home,
		children: [
			{
				path:'',
				redirect:'direct-chats',
				name: 'Home',
			},
			{
				path: 'direct-chats',
				component: HomeDirectChats,
				name: "HomeDirectChats",
			},
			{
				path: 'groups',
				component: HomeGroups,
				name: "HomeGroups",
			}
		]
	},
	{
		path: '/chat/:chatId',
		component: DirectChat,
		name: 'DirectChat'
	},
	{
		path: '',
		name: 'FrontPage',
		component: FrontPage,
	}
]

const router = new VueRouter({
	mode: 'history',
	base: process.env.BASE_URL,
	routes
})

router.beforeEach((to, from, next) => {

	if(to.path.startsWith('/home') || to.path.startsWith('/chat') || to.path.startsWith('/group')){

		if(store.state.token === false){

			var cookie = (document.cookie.match('(^|;)\\s*' + "token" + '\\s*=\\s*([^;]+)')?.pop() || '').split(',')[0];

			if(cookie !== ''){

				window.axios.post(Vue.prototype.$apiHttpUrl + '/api/auth/validate',
					new URLSearchParams({
							'token': cookie
					})
				).then((response) => {

					store.commit('setToken', cookie);
					store.commit('setUserId', response.data.userId);
					next();

				}, () => {

					document.cookie = 'token=; Max-Age=-99999999;';
					next('/');

				})
			}
			else{
				next('/');
			}
		}
		else{
			next();
		}
	}
	else{
		next();
	}
});

export default router
