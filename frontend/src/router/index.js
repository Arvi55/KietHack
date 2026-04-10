import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Signup from '../views/Signup.vue'
import ProfileSetup from '../views/ProfileSetup.vue'
import Quiz from '../views/Quiz.vue'
import Dashboard from '../views/Dashboard.vue'
import Chat from '../views/Chat.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    { path: '/login', name: 'login', component: Login },
    { path: '/signup', name: 'signup', component: Signup },
    { path: '/profile-setup', name: 'profileSetup', component: ProfileSetup },
    { path: '/quiz', name: 'quiz', component: Quiz },
    { path: '/dashboard', name: 'dashboard', component: Dashboard },
    { path: '/chat', name: 'chat', component: Chat }
  ]
})

export default router
