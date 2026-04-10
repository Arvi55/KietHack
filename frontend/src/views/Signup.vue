<template>
  <div
    class="app-container min-h-screen w-full bg-gradient-to-br from-cream via-sand to-[#E5D9CF] text-forest"
  >
    <div
      class="auth-wrapper w-full max-w-md rounded-3xl border border-forest/10 bg-white/90 px-6 py-10 shadow-lg shadow-forest/10 backdrop-blur-sm sm:px-10 sm:py-12"
    >
      <div class="mb-6 text-center">
        <h1 class="mb-2 font-serif text-3xl font-semibold text-forest sm:text-4xl">Join Emotion Buddy</h1>
        <p class="text-sm text-forest/60 sm:text-base">
          Create an account to start configuring your companion
        </p>
      </div>

      <form @submit.prevent="handleSignup">
        <div class="form-group">
          <label class="mb-2 block text-sm text-forest/65" for="name">Name</label>
          <input
            id="name"
            v-model="name"
            type="text"
            class="auth-input"
            required
            placeholder="Display Name"
          />
        </div>
        <div class="form-group">
          <label class="mb-2 block text-sm text-forest/65" for="email">Email</label>
          <input
            id="email"
            v-model="email"
            type="email"
            class="auth-input"
            required
            placeholder="you@example.com"
          />
        </div>
        <div class="form-group">
          <label class="mb-2 block text-sm text-forest/65" for="password">Password</label>
          <input
            id="password"
            v-model="password"
            type="password"
            class="auth-input"
            required
            placeholder="••••••••"
          />
        </div>

        <p v-if="errorMessage" class="mb-4 text-center text-sm text-red-600">{{ errorMessage }}</p>

        <button type="submit" class="btn-primary mt-2" :disabled="isLoading">
          {{ isLoading ? 'Creating Account...' : 'Sign Up' }}
        </button>
      </form>

      <p class="mt-8 text-center text-sm text-forest/60">
        Already have an account?
        <router-link to="/login" class="font-medium text-lime no-underline transition hover:text-forest"
          >Log in</router-link
        >
      </p>
      <p class="mt-4 text-center">
        <router-link
          to="/"
          class="text-sm text-forest/45 no-underline transition hover:text-forest/75"
          >← Back to home</router-link
        >
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
      password: password.value,
    })

    localStorage.setItem('userId', response.data.id)
    localStorage.setItem('userName', response.data.name)

    router.push('/profile-setup')
  } catch (error) {
    errorMessage.value = 'Registration failed. Email might already be taken.'
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.form-group {
  margin-bottom: 1.5rem;
}

.auth-input {
  width: 100%;
  border-radius: 0.75rem;
  border: 1px solid rgb(69 61 55 / 0.14);
  background: rgb(255 250 245 / 0.95);
  padding: 0.875rem 1rem;
  font-size: 1rem;
  color: #453d37;
  outline: none;
  transition:
    border-color 0.2s,
    box-shadow 0.2s;
}

.auth-input::placeholder {
  color: rgb(69 61 55 / 0.32);
}

.auth-input:focus {
  border-color: rgb(184 105 94 / 0.55);
  box-shadow: 0 0 0 2px rgb(215 180 174 / 0.35);
}

.auth-wrapper {
  animation: fadeUp 0.65s ease-out both;
}

@keyframes fadeUp {
  from {
    opacity: 0;
    transform: translateY(18px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
