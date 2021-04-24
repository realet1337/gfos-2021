import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store/'

import Login from '@/views/Login.vue'
import Home from '@/views/Home.vue'
import FrontPage from '@/views/FrontPage.vue'
import DirectChat from '@/views/DirectChat.vue'
import Group from '@/views/Group.vue'
import Register from '@/views/Register.vue'
import GroupEdit from '@/views/GroupEdit.vue'
import Edit from '@/views/Edit.vue'

import HomeDirectChats from '@/views/HomeDirectChats.vue'
import HomeGroups from '@/views/HomeGroups.vue'

import GroupEditOverview from '@/views/GroupEditOverview'
import GroupEditUsersView from '@/views/GroupEditUsersView'
import GroupEditRolesView from '@/views/GroupEditRolesView'
import GroupEditPermissionsView from '@/views/GroupEditPermissionsView'

import EditProfile from '@/views/EditProfile'
import EditSettings from '@/views/EditSettings'

Vue.use(VueRouter)

const routes = [
	{
		path: '/login',
		name: 'Login',
		component: Login
	},
	{
		path: '/register',
		name: 'Register',
		component: Register
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
		path: '/group/:groupId/chat/:chatId',
		component: Group,
		name: 'GroupChat'
	},
	{
		path: '/group/:groupId',
		component: Group,
		name: 'Group'
	},
	{
		path: '/group/:groupId/edit',
		component: GroupEdit,
		children: [
			{
				path:'overview',
				component: GroupEditOverview,
				name: 'GroupEditOverview',
			},
			{
				path: 'roles',
				component: GroupEditRolesView,
				name: "GroupEditRolesView",
			},
			{
				path: 'permissions',
				component: GroupEditPermissionsView,
				name: "GroupEditPermissionsView",
			},
			{
				path: 'users',
				component: GroupEditUsersView,
				name: "GroupEditUsersView",
			}
		]
	},
	{
		path: '/edit',
		component: Edit,
		children: [
			{
				path: 'profile',
				component: EditProfile,
				name: 'EditProfile'
			},
			{
				path: 'settings',
				component: EditSettings,
				name: 'EditSettings'
			},
		]
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


	if(to.path.startsWith('/home') || to.path.startsWith('/chat') || to.path.startsWith('/group') || to.path.startsWith('/edit')){

		if(store.state.token === false){

			var cookie = (document.cookie.match('(^|;)\\s*' + "token" + '\\s*=\\s*([^;]+)')?.pop() || '').split(',')[0];

			if(cookie !== ''){

				window.axios.post(Vue.prototype.$getApiUrl('http') + '/auth/validate',
					new URLSearchParams({
							'token': cookie
					})
				).then((response) => {

					store.commit('setToken', cookie);
					store.commit('setUserId', response.data.userId);
					Vue.prototype.$initApp();
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
			if(!store.state.initialized){
				Vue.prototype.$initApp();
			}
			next();
		}
	}
	else{
		if(store.state.initialized){
			store.state.ws.close();
			store.commit('reset');
		}
		next();
	}
});

export default router
