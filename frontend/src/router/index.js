import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Signup from '../views/Signup.vue'
import ProfileSetup from '../views/ProfileSetup.vue'
import Quiz from '../views/Quiz.vue'
import Dashboard from '../views/Dashboard.vue'
import Chat from '../views/Chat.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: Home },
    { path: '/login', name: 'login', component: Login },
    { path: '/signup', name: 'signup', component: Signup },
    { path: '/profile-setup', name: 'profileSetup', component: ProfileSetup },
    { path: '/quiz', name: 'quiz', component: Quiz },
    { path: '/dashboard', name: 'dashboard', component: Dashboard, meta: { requiresAuth: true } },
    { path: '/chat', name: 'chat', component: Chat, meta: { requiresAuth: true } }
  ]
})

router.beforeEach((to, from, next) => {
  const userId = localStorage.getItem('userId')
  const isAuthenticated = userId && userId !== 'null' && userId !== 'undefined'
  
  if (to.meta.requiresAuth && !isAuthenticated) {
    alert(`To access ${to.name || 'this page'}, please log in first.`)
    next({ name: 'login' })
  } else {
    next()
  }
})

export default router
