<template>
  <div class="auth-wrapper">
    <div class="glass-panel auth-card">
      <div class="text-center mb-8">
        <h1 class="logo-text">AI Buddy</h1>
        <p class="subtitle">Securely log in to continue your mental health journey</p>
      </div>

      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="email">Email</label>
          <input type="email" id="email" v-model="email" class="input-field" required placeholder="you@example.com" />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" id="password" v-model="password" class="input-field" required placeholder="••••••••" />
        </div>
        
        <p v-if="errorMessage" class="error-text">{{ errorMessage }}</p>
        
        <button type="submit" class="btn-primary mt-4" :disabled="isLoading">
          {{ isLoading ? 'Logging in...' : 'Log In' }}
        </button>
      </form>

      <p class="text-center mt-6 switch-text">
        Don't have an account? <router-link to="/signup" class="link">Sign up</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const email = ref('')
const password = ref('')
const errorMessage = ref('')
const isLoading = ref(false)

const handleLogin = async () => {
  try {
    isLoading.value = true
    errorMessage.value = ''
    
    const response = await axios.post('http://localhost:8080/auth/login', {
      email: email.value,
      password: password.value
    })
    
    // Save user ID to localStorage or simple state
    localStorage.setItem('userId', response.data.id)
    localStorage.setItem('userName', response.data.name)
    
    // For now redirecting to profile-setup, will improve logic later
    router.push('/dashboard')
    
  } catch (error) {
    if (error.response && error.response.status === 401) {
      errorMessage.value = 'Invalid email or password'
    } else {
      errorMessage.value = 'An error occurred. Please try again later.'
    }
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.auth-wrapper {
  width: 100%;
  max-width: 450px;
  animation: fadeUp 0.6s ease-out forwards;
}

.auth-card {
  padding: 3rem;
}

.text-center { text-align: center; }
.mb-8 { margin-bottom: 2rem; }
.mt-4 { margin-top: 1rem; }
.mt-6 { margin-top: 1.5rem; }

.logo-text {
  font-size: 2.5rem;
  font-weight: 700;
  background: linear-gradient(to right, var(--primary), var(--secondary));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 0.5rem;
}

.subtitle { color: var(--text-secondary); font-size: 0.95rem; }
.error-text { color: var(--error); font-size: 0.9rem; margin-bottom: 1rem; text-align: center; }
.switch-text { color: var(--text-secondary); font-size: 0.9rem; }

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
