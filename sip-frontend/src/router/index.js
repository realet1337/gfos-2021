import Vue from 'vue'
import VueRouter from 'vue-router'

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

export default router
