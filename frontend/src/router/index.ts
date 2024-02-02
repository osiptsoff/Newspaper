import { createRouter, createWebHistory } from 'vue-router'

import Main from '../views/Main.vue'
import Loginuser from "@/views/Loginuser.vue";
import NewPost from "@/components/NewPost.vue";



const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: Main,
    },
    {
      path: '/login',
      component: Loginuser,
    },
    {
      path : '/newpost',
      component: NewPost,
    }
    ]
})

export default router

