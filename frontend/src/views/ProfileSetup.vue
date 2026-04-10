<template>
  <div class="app-container">
    <div class="glass-panel profile-card">
      <div class="text-center mb-6">
        <h2 class="title">Tell Us About Yourself</h2>
        <p class="subtitle">Step {{ currentStep }} of {{ totalSteps }}</p>
        
        <div class="progress-bar-container mt-4">
          <div class="progress-bar" :style="{ width: progressPercentage + '%' }"></div>
        </div>
      </div>

      <form @submit.prevent="nextStep">
        
        <!-- Step 1 -->
        <div v-if="currentStep === 1" class="step-container">
          <div class="form-group">
            <label>What are your hobbies?</label>
            <input type="text" v-model="formData.hobbies" class="input-field" placeholder="E.g., Gaming, Reading, Music" required />
          </div>
          <div class="form-group">
            <label>What are your favorite activities?</label>
            <input type="text" v-model="formData.favoriteActivities" class="input-field" placeholder="E.g., Hiking, Cooking" required />
          </div>
        </div>

        <!-- Step 2 -->
        <div v-if="currentStep === 2" class="step-container">
          <div class="form-group">
            <label>Favorite Movie Genres?</label>
            <input type="text" v-model="formData.favoriteMovieGenres" class="input-field" placeholder="E.g., Sci-Fi, Romance, Comedy" required />
          </div>
          <div class="form-group">
            <label>Favorite Music Type?</label>
            <input type="text" v-model="formData.favoriteMusicType" class="input-field" placeholder="E.g., Pop, Classical, Rock" required />
          </div>
          <div class="form-group">
            <label>Favorite Food?</label>
            <input type="text" v-model="formData.favoriteFood" class="input-field" placeholder="E.g., Pizza, Sushi" required />
          </div>
        </div>

        <!-- Step 3 -->
        <div v-if="currentStep === 3" class="step-container">
          <div class="form-group">
            <label>Comfort Activities (What do you do when sad)?</label>
            <input type="text" v-model="formData.comfortActivities" class="input-field" placeholder="E.g., Listen to sad music, sleep" required />
          </div>
          <div class="form-group">
            <label>Social Preference</label>
            <select v-model="formData.socialPreference" class="input-field select-field" required>
              <option value="" disabled>Select an option</option>
              <option value="Alone">I prefer being alone</option>
              <option value="With People">I prefer being with people</option>
              <option value="Mixed">It depends</option>
            </select>
          </div>
        </div>

        <div class="button-group mt-6">
          <button type="button" v-if="currentStep > 1" @click="prevStep" class="btn-secondary">Back</button>
          
          <button type="submit" v-if="currentStep < totalSteps" class="btn-primary" style="flex: 1;">Next</button>
          <button type="button" v-if="currentStep === totalSteps" @click="submitProfile" class="btn-primary" :disabled="isLoading" style="flex: 1;">
            {{ isLoading ? 'Saving...' : 'Finish Setup' }}
          </button>
        </div>
      </form>
      <p v-if="errorMessage" class="error-text mt-4">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const currentStep = ref(1)
const totalSteps = 3
const isLoading = ref(false)
const errorMessage = ref('')

const formData = ref({
  hobbies: '',
  favoriteActivities: '',
  favoriteMovieGenres: '',
  favoriteMusicType: '',
  favoriteFood: '',
  comfortActivities: '',
  socialPreference: ''
})

const progressPercentage = computed(() => {
  return (currentStep.value / totalSteps) * 100
})

const nextStep = () => {
  if (currentStep.value < totalSteps) {
    currentStep.value++
  }
}

const prevStep = () => {
  if (currentStep.value > 1) {
    currentStep.value--
  }
}

const submitProfile = async () => {
  try {
    isLoading.value = true
    errorMessage.value = ''
    
    let userId = localStorage.getItem('userId')
    if (!userId) {
      errorMessage.value = 'User ID not found. Please log in again.'
      return
    }

    await axios.post('http://localhost:8080/profile/save', {
      userId: parseInt(userId),
      ...formData.value
    })

    router.push('/quiz')
  } catch (error) {
    errorMessage.value = 'Error saving profile. Please try again.'
  } finally {
    isLoading.value = false
  }
}
</script>

<style scoped>
.app-container {
  width: 100%;
}
.profile-card {
  max-width: 600px;
  width: 100%;
  animation: fadeUp 0.6s ease-out forwards;
}

.title {
  font-size: 2rem;
  color: var(--text-primary);
  margin-bottom: 0.5rem;
}
.text-center { text-align: center; }
.mb-6 { margin-bottom: 1.5rem; }
.mt-4 { margin-top: 1rem; }
.mt-6 { margin-top: 1.5rem; }
.subtitle { color: var(--text-secondary); }

.progress-bar-container {
  width: 100%;
  height: 6px;
  background: var(--surface-border);
  border-radius: 4px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, var(--primary), var(--secondary));
  transition: width 0.3s ease;
}

.step-container {
  animation: fadeIn 0.4s ease;
}

.button-group {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
}

.btn-secondary {
  background: transparent;
  color: var(--text-primary);
  border: 1px solid var(--surface-border);
  padding: 0.875rem 1.5rem;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.btn-secondary:hover {
  background: var(--surface-border);
}

.select-field option {
  background: var(--bg-gradient-end);
  color: white;
}

.error-text {
  color: var(--error);
  text-align: center;
}

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}
</style>
