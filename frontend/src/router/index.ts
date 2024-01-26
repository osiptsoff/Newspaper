import { createRouter, createWebHistory } from 'vue-router'

import Main from '../views/Main.vue'
import Login from '../views/Login.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: Main,
    },
    {
      path: '/login',
      component: Login,
    }
  ]
})

export default router
