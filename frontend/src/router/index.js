import { createRouter, createWebHistory } from 'vue-router'

import Login from '../components/Login.vue'
import ContactList from '../components/ContactList.vue'
import ContactDetail from '../ContactDetail.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/',
    name: 'Home',
    component: ContactList,
    meta: { requiresAuth: true }
  },
  {
    path: '/contact/:id',
    name: 'ContactDetail',
    component: ContactDetail,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to) => {
  const token = localStorage.getItem('token')

  if (to.meta.requiresAuth && !token) {
    return '/login'
  }

  if (to.path === '/login' && token) {
    return '/'
  }

  return true
})

export default router