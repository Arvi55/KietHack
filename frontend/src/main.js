import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'

// API base URL - change this for different environments
window.API_BASE_URL = 'https://kiethack-5.onrender.com'

const app = createApp(App)
app.use(router)
app.mount('#app')
