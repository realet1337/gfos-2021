import Vue from 'vue'
import VueRouter from 'vue-router'

import Login from '@/views/Login.vue'
import Home from '@/views/Home.vue'
import FrontPage from '@/views/FrontPage.vue'

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
    name: 'Home',
    component: Home,
    children: [
      {
        path:'',
        redirect:'direct-chats'
      },
      {
        path: 'direct-chats',
        component: HomeDirectChats
      },
      {
        path: 'groups',
        component: HomeGroups
      }
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

export default router
