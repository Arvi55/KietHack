<template>
  <div class="auth-wrapper">
    <div class="glass-panel auth-card">
      <div class="text-center mb-6">
        <h1 class="logo-text">Join AI Buddy</h1>
        <p class="subtitle">Create an account to start configuring your companion</p>
      </div>

      <form @submit.prevent="handleSignup">
        <div class="form-group">
          <label for="name">Name</label>
          <input type="text" id="name" v-model="name" class="input-field" required placeholder="Display Name" />
        </div>
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
          {{ isLoading ? 'Creating Account...' : 'Sign Up' }}
        </button>
      </form>

      <p class="text-center mt-6 switch-text">
        Already have an account? <router-link to="/login" class="link">Log in</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const name = ref('')
const email = ref('')
const password = ref('')
const errorMessage = ref('')
const isLoading = ref(false)

const handleSignup = async () => {
  try {
    isLoading.value = true
    errorMessage.value = ''
    
    const response = await axios.post('http://localhost:8080/auth/signup', {
      name: name.value,
      email: email.value,
      password: password.value
    })
    
    // Save user ID to localStorage
    localStorage.setItem('userId', response.data.id)
    localStorage.setItem('userName', response.data.name)
    
    // Redirect to profile setup step
    router.push('/profile-setup')
    
  } catch (error) {
    errorMessage.value = 'Registration failed. Email might already be taken.'
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

.auth-card { padding: 2.5rem; }
.text-center { text-align: center; }
.mb-6 { margin-bottom: 1.5rem; }
.mt-4 { margin-top: 1rem; }
.mt-6 { margin-top: 1.5rem; }

.logo-text {
  font-size: 2.2rem;
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
