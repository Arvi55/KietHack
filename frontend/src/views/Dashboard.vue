<template>
  <div class="app-container dashboard-wrapper">
    <div class="glass-panel profile-summary">
      <div class="text-center mb-6">
        <h2 class="title">Welcome, {{ userName }}!</h2>
        <div class="personality-badge" v-if="personalityType">
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
        <h3 class="mb-4">Select your AI Buddy Persona</h3>
        <p class="subtitle mb-6 text-sm">Who do you want to talk to today?</p>
        
        <div class="roles-grid">
          <button 
            v-for="role in roles" 
            :key="role"
            @click="selectRole(role)"
            :class="['btn-role', { 'btn-role-active': selectedRole === role }]"
          >
            {{ role }}
          </button>
        </div>

        <button 
          @click="startChat" 
          class="btn-primary mt-8" 
          :disabled="!selectedRole || isLoading"
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
      role: selectedRole.value
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
  padding-top: 5rem;
}

.profile-summary {
  width: 100%;
  max-width: 700px;
  animation: fadeUp 0.6s ease-out forwards;
}

.title { font-size: 2.2rem; background: linear-gradient(to right, var(--primary), var(--secondary)); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }
.text-center { text-align: center; }
.mb-6 { margin-bottom: 1.5rem; }
.mb-4 { margin-bottom: 1rem; }
.mt-4 { margin-top: 1rem; }
.mt-6 { margin-top: 1.5rem; }
.mt-8 { margin-top: 2rem; }
.subtitle { color: var(--text-secondary); }
.text-sm { font-size: 0.9rem; }

.personality-badge {
  display: inline-block;
  background: rgba(99, 102, 241, 0.2);
  color: #a5b4fc;
  border: 1px solid rgba(99, 102, 241, 0.5);
  padding: 0.5rem 1.5rem;
  border-radius: 99px;
  font-weight: 700;
  font-size: 1.2rem;
  letter-spacing: 2px;
  margin-top: 1rem;
}

.data-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1rem;
  background: rgba(0, 0, 0, 0.2);
  padding: 1.5rem;
  border-radius: 16px;
}

@media (min-width: 600px) {
  .data-grid { grid-template-columns: 1fr 1fr; }
}

.data-item { color: white; font-size: 0.95rem; }
.data-item strong { color: var(--text-secondary); }

.roles-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1rem;
}

@media (min-width: 600px) {
  .roles-grid { grid-template-columns: repeat(3, 1fr); }
}

.btn-role {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid var(--surface-border);
  border-radius: 12px;
  color: white;
  padding: 1rem;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-role:hover {
  background: rgba(255, 255, 255, 0.1);
  transform: translateY(-2px);
}

.btn-role-active {
  background: rgba(99, 102, 241, 0.2);
  border-color: var(--primary);
  box-shadow: 0 0 15px rgba(99, 102, 241, 0.3);
}

.error-text { color: var(--error); margin-top: 1rem; }

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
