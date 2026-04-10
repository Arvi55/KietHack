<template>
  <div
    class="app-container dashboard-wrapper min-h-screen w-full bg-gradient-to-br from-cream via-sand to-[#E5D9CF] text-forest"
  >
    <div class="profile-summary">
      <div class="mb-6 text-center">
        <h2 class="title font-serif">Welcome, {{ userName }}!</h2>
        <div v-if="personalityType" class="personality-badge">
          {{ personalityType }}
        </div>
        <p class="subtitle mt-4">We've customized your experience based on your preferences.</p>
      </div>

      <div class="data-grid mt-6">
        <div class="data-item">
          <strong>Hobbies:</strong> {{ profileData.hobbies || 'Not provided' }}
        </div>
        <div class="data-item">
          <strong>Comfort Activity:</strong> {{ profileData.comfortActivities || 'Not provided' }}
        </div>
        <div class="data-item">
          <strong>Social:</strong> {{ profileData.socialPreference || 'Not provided' }}
        </div>
      </div>

      <div class="role-selection mt-8 text-center">
        <h3 class="mb-4 text-lg font-semibold text-forest">Select your Emotion Buddy persona</h3>
        <p class="subtitle mb-6 text-sm">Who do you want to talk to today?</p>

        <div class="roles-grid">
          <button
            v-for="role in roles"
            :key="role"
            type="button"
            :class="['btn-role', { 'btn-role-active': selectedRole === role }]"
            @click="selectRole(role)"
          >
            {{ role }}
          </button>
        </div>

        <button
          type="button"
          class="btn-primary mt-8"
          :disabled="!selectedRole || isLoading"
          @click="startChat"
        >
          {{ isLoading ? 'Entering...' : 'Start Chatting' }}
        </button>
        <p v-if="errorMessage" class="error-text mt-4">{{ errorMessage }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const userName = ref(localStorage.getItem('userName') || 'User')
const personalityType = ref('')
const profileData = ref({})
const selectedRole = ref('')
const isLoading = ref(false)
const errorMessage = ref('')

const roles = ['Friend', 'Mother', 'Father', 'Girlfriend', 'Boyfriend', 'Coach']

onMounted(async () => {
  const userId = localStorage.getItem('userId')
  if (!userId) {
    router.push('/login')
    return
  }

  try {
    const profileRes = await axios.get(`http://localhost:8080/profile/${userId}`)
    profileData.value = profileRes.data || {}
    if (profileRes.data && profileRes.data.aiBuddyRole) {
      selectedRole.value = profileRes.data.aiBuddyRole
    }

    const quizRes = await axios.get(`http://localhost:8080/quiz/${userId}`)
    if (quizRes.data && quizRes.data.personalityType) {
      personalityType.value = quizRes.data.personalityType
    }
  } catch (error) {
    console.error('Error fetching dashboard data:', error)
  }
})

const selectRole = (role) => {
  selectedRole.value = role
}

const startChat = async () => {
  if (!selectedRole.value) return

  try {
    isLoading.value = true
    errorMessage.value = ''

    const userId = localStorage.getItem('userId')

    await axios.post('http://localhost:8080/profile/set-role', {
      userId: parseInt(userId),
      role: selectedRole.value,
    })

    localStorage.setItem('aiBuddyRole', selectedRole.value)
    router.push('/chat')
  } catch (error) {
    errorMessage.value = 'Failed to set AI role. Please try again.'
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.dashboard-wrapper {
  align-items: flex-start;
  padding-top: 3rem;
}

.profile-summary {
  width: 100%;
  max-width: 700px;
  border-radius: 1.5rem;
  border: 1px solid rgb(69 61 55 / 0.1);
  background: rgb(255 255 255 / 0.92);
  padding: 2rem;
  box-shadow:
    0 10px 40px rgb(69 61 55 / 0.08),
    0 2px 12px rgb(69 61 55 / 0.04);
  backdrop-filter: blur(10px);
  animation: fadeUp 0.6s ease-out forwards;
}

@media (min-width: 640px) {
  .profile-summary {
    padding: 2.5rem;
  }
}

.title {
  font-size: 2.2rem;
  background: linear-gradient(to right, #453d37, #b8695e);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.text-center {
  text-align: center;
}
.mb-6 {
  margin-bottom: 1.5rem;
}
.mb-4 {
  margin-bottom: 1rem;
}
.mt-4 {
  margin-top: 1rem;
}
.mt-6 {
  margin-top: 1.5rem;
}
.mt-8 {
  margin-top: 2rem;
}

.subtitle {
  color: rgb(69 61 55 / 0.58);
}

.text-sm {
  font-size: 0.9rem;
}

.personality-badge {
  display: inline-block;
  margin-top: 1rem;
  border-radius: 999px;
  border: 1px solid rgb(184 105 94 / 0.35);
  background: rgb(215 180 174 / 0.2);
  padding: 0.5rem 1.5rem;
  font-size: 1.2rem;
  font-weight: 700;
  letter-spacing: 2px;
  color: #8f5349;
}

.data-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1rem;
  border-radius: 16px;
  border: 1px solid rgb(69 61 55 / 0.08);
  background: rgb(255 250 245 / 0.65);
  padding: 1.5rem;
}

@media (min-width: 600px) {
  .data-grid {
    grid-template-columns: 1fr 1fr;
  }
}

.data-item {
  font-size: 0.95rem;
  color: #453d37;
}

.data-item strong {
  font-weight: 600;
  color: rgb(69 61 55 / 0.5);
}

.roles-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1rem;
}

@media (min-width: 600px) {
  .roles-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

.btn-role {
  cursor: pointer;
  border-radius: 12px;
  border: 1px solid rgb(69 61 55 / 0.12);
  background: rgb(255 250 245 / 0.75);
  padding: 1rem;
  font-size: 1rem;
  font-weight: 600;
  color: #453d37;
  transition: all 0.2s ease;
}

.btn-role:hover {
  background: rgb(255 255 255 / 0.95);
  transform: translateY(-2px);
  border-color: rgb(184 105 94 / 0.25);
}

.btn-role-active {
  border-color: #b8695e;
  background: rgb(184 105 94 / 0.12);
  box-shadow: 0 0 0 1px rgb(184 105 94 / 0.2);
}

.error-text {
  margin-top: 1rem;
  font-size: 0.95rem;
  color: #c24133;
}

@keyframes fadeUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
