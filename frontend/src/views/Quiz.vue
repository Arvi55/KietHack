<template>
  <div class="app-container">
    <div class="glass-panel quiz-card">
      <div v-if="!isFinished" class="quiz-content">
        <div class="text-center mb-6">
          <h2 class="title">Personality Quiz</h2>
          <p class="subtitle">Question {{ currentQuestionIndex + 1 }} of {{ questions.length }}</p>
          <div class="progress-bar-container mt-4">
            <div class="progress-bar" :style="{ width: progressPercentage + '%' }"></div>
          </div>
        </div>

        <div class="question-section mt-6">
          <h3 class="question-text">{{ currentQuestion.text }}</h3>
          
          <div class="options-container mt-6">
            <button 
              v-for="(option, index) in currentQuestion.options" 
              :key="index"
              @click="selectOption(option)"
              class="btn-option"
            >
              {{ option.text }}
            </button>
          </div>
        </div>
      </div>
      
      <div v-else class="quiz-content text-center">
        <h2 class="title mb-4">Quiz Complete!</h2>
        <p class="subtitle mb-6">We are analyzing your personality profile...</p>
        <p v-if="errorMessage" class="error-text">{{ errorMessage }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const isFinished = ref(false)
const errorMessage = ref('')
const currentQuestionIndex = ref(0)

const scores = ref({
  E: 0, I: 0,
  S: 0, N: 0,
  T: 0, F: 0
})

const questions = [
  {
    text: "At a social event, you usually:",
    options: [
      { text: "Interact with many, including strangers", type: 'E' },
      { text: "Interact with a few people you know well", type: 'I' }
    ]
  },
  {
    text: "To recharge your energy, you need to:",
    options: [
      { text: "Go out and do activities with people", type: 'E' },
      { text: "Spend quiet time alone", type: 'I' }
    ]
  },
  {
    text: "You prefer being around people who are:",
    options: [
      { text: "Practical and realistic", type: 'S' },
      { text: "Imaginative and creative", type: 'N' }
    ]
  },
  {
    text: "When reading a book, what do you notice first?",
    options: [
      { text: "Facts, details, and events", type: 'S' },
      { text: "Underlying themes and hidden meanings", type: 'N' }
    ]
  },
  {
    text: "You usually make decisions based on:",
    options: [
      { text: "Logic, objective analysis, and facts", type: 'T' },
      { text: "Empathy, personal values, and feelings", type: 'F' }
    ]
  },
  {
    text: "When resolving a conflict, you prioritize:",
    options: [
      { text: "Justice and fairness", type: 'T' },
      { text: "Harmony and people's feelings", type: 'F' }
    ]
  },
  {
    text: "Do you prefer working on:",
    options: [
      { text: "Concrete, tangible tasks with clear steps", type: 'S' },
      { text: "Abstract, conceptual tasks", type: 'N' }
    ]
  },
  {
    text: "When someone shares a problem, you tend to:",
    options: [
      { text: "Offer logical solutions and try to fix it", type: 'T' },
      { text: "Offer emotional support and listen", type: 'F' }
    ]
  },
  {
    text: "In a tricky situation, you trust:",
    options: [
      { text: "Your past experience", type: 'S' },
      { text: "Your gut instinct", type: 'N' }
    ]
  },
  {
    text: "When starting a conversation, you usually:",
    options: [
      { text: "Jump right in and talk freely", type: 'E' },
      { text: "Think before you speak or wait to be approached", type: 'I' }
    ]
  }
]

const currentQuestion = computed(() => questions[currentQuestionIndex.value])
const progressPercentage = computed(() => (currentQuestionIndex.value / questions.length) * 100)

const selectOption = async (option) => {
  scores.value[option.type]++
  
  if (currentQuestionIndex.value < questions.length - 1) {
    currentQuestionIndex.value++
  } else {
    isFinished.value = true
    await submitQuiz()
  }
}

const submitQuiz = async () => {
  try {
    const userId = localStorage.getItem('userId')
    if (!userId) {
      errorMessage.value = "User session not found."
      return
    }

    const payload = {
      userId: parseInt(userId),
      extrovertScore: scores.value.E,
      introvertScore: scores.value.I,
      sensingScore: scores.value.S,
      intuitionScore: scores.value.N,
      thinkingScore: scores.value.T,
      feelingScore: scores.value.F
    }

    await axios.post('http://localhost:8080/quiz/submit', payload)
    
    // Redirect to Dashboard after saving
    setTimeout(() => {
      router.push('/dashboard')
    }, 1500)
    
  } catch (error) {
    errorMessage.value = "Failed to submit quiz results. Please refresh and try again."
  }
}
</script>

<style scoped>
.app-container { width: 100%; }
.quiz-card {
  max-width: 600px;
  width: 100%;
  animation: fadeUp 0.6s ease-out forwards;
}

.title { font-size: 2rem; color: var(--text-primary); margin-bottom: 0.5rem; }
.text-center { text-align: center; }
.mb-6 { margin-bottom: 1.5rem; }
.mb-4 { margin-bottom: 1rem; }
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

.question-text {
  font-size: 1.25rem;
  font-weight: 500;
  line-height: 1.6;
  text-align: center;
  color: var(--text-primary);
}

.options-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.btn-option {
  width: 100%;
  padding: 1.25rem 1.5rem;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid var(--surface-border);
  border-radius: 12px;
  color: white;
  font-size: 1rem;
  text-align: left;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-option:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: var(--primary);
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(99, 102, 241, 0.2);
}

.error-text { color: var(--error); margin-top: 1rem; }

@keyframes fadeUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
