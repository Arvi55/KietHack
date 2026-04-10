<template>
  <div
    class="app-container min-h-screen w-full bg-gradient-to-br from-cream via-sand to-[#E5D9CF] text-forest"
  >
    <div class="quiz-card">
      <div v-if="!isFinished" class="quiz-content">
        <div class="mb-6 text-center">
          <h2 class="title font-serif">Personality Quiz</h2>
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
              type="button"
              class="btn-option"
              @click="selectOption(option)"
            >
              {{ option.text }}
            </button>
          </div>
        </div>
      </div>

      <div v-else class="quiz-content text-center">
        <h2 class="title mb-4 font-serif">Quiz Complete!</h2>
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
  E: 0,
  I: 0,
  S: 0,
  N: 0,
  T: 0,
  F: 0,
})

const questions = [
  {
    text: 'At a social event, you usually:',
    options: [
      { text: 'Interact with many, including strangers', type: 'E' },
      { text: 'Interact with a few people you know well', type: 'I' },
    ],
  },
  {
    text: 'To recharge your energy, you need to:',
    options: [
      { text: 'Go out and do activities with people', type: 'E' },
      { text: 'Spend quiet time alone', type: 'I' },
    ],
  },
  {
    text: 'You prefer being around people who are:',
    options: [
      { text: 'Practical and realistic', type: 'S' },
      { text: 'Imaginative and creative', type: 'N' },
    ],
  },
  {
    text: 'When reading a book, what do you notice first?',
    options: [
      { text: 'Facts, details, and events', type: 'S' },
      { text: 'Underlying themes and hidden meanings', type: 'N' },
    ],
  },
  {
    text: 'You usually make decisions based on:',
    options: [
      { text: 'Logic, objective analysis, and facts', type: 'T' },
      { text: 'Empathy, personal values, and feelings', type: 'F' },
    ],
  },
  {
    text: 'When resolving a conflict, you prioritize:',
    options: [
      { text: 'Justice and fairness', type: 'T' },
      { text: "Harmony and people's feelings", type: 'F' },
    ],
  },
  {
    text: 'Do you prefer working on:',
    options: [
      { text: 'Concrete, tangible tasks with clear steps', type: 'S' },
      { text: 'Abstract, conceptual tasks', type: 'N' },
    ],
  },
  {
    text: 'When someone shares a problem, you tend to:',
    options: [
      { text: 'Offer logical solutions and try to fix it', type: 'T' },
      { text: 'Offer emotional support and listen', type: 'F' },
    ],
  },
  {
    text: 'In a tricky situation, you trust:',
    options: [
      { text: 'Your past experience', type: 'S' },
      { text: 'Your gut instinct', type: 'N' },
    ],
  },
  {
    text: 'When starting a conversation, you usually:',
    options: [
      { text: 'Jump right in and talk freely', type: 'E' },
      { text: 'Think before you speak or wait to be approached', type: 'I' },
    ],
  },
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
      errorMessage.value = 'User session not found.'
      return
    }

    const payload = {
      userId: parseInt(userId),
      extrovertScore: scores.value.E,
      introvertScore: scores.value.I,
      sensingScore: scores.value.S,
      intuitionScore: scores.value.N,
      thinkingScore: scores.value.T,
      feelingScore: scores.value.F,
    }

    await axios.post(`${window.API_BASE_URL}/quiz/submit`, payload)

    setTimeout(() => {
      router.push('/dashboard')
    }, 1500)
  } catch (error) {
    errorMessage.value = 'Failed to submit quiz results. Please refresh and try again.'
  }
}
</script>

<style scoped>
.quiz-card {
  max-width: 600px;
  width: 100%;
  border-radius: 1.5rem;
  border: 1px solid rgb(69 61 55 / 0.1);
  background: rgb(255 255 255 / 0.9);
  padding: 2rem;
  box-shadow:
    0 10px 40px rgb(69 61 55 / 0.08),
    0 2px 12px rgb(69 61 55 / 0.04);
  backdrop-filter: blur(8px);
  animation: fadeUp 0.6s ease-out forwards;
}

@media (min-width: 640px) {
  .quiz-card {
    padding: 2.5rem;
  }
}

.title {
  font-size: 2rem;
  margin-bottom: 0.5rem;
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

.subtitle {
  color: rgb(69 61 55 / 0.58);
  font-size: 0.95rem;
}

.progress-bar-container {
  width: 100%;
  height: 6px;
  background: rgb(237 227 217 / 0.85);
  border-radius: 4px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, #b8695e, #d7b4ae);
  transition: width 0.3s ease;
}

.question-text {
  font-size: 1.25rem;
  font-weight: 500;
  line-height: 1.6;
  text-align: center;
  color: #453d37;
}

.options-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.btn-option {
  width: 100%;
  padding: 1.25rem 1.5rem;
  background: rgb(255 250 245 / 0.75);
  border: 1px solid rgb(69 61 55 / 0.12);
  border-radius: 12px;
  color: #453d37;
  font-size: 1rem;
  text-align: left;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-option:hover {
  background: rgb(255 255 255 / 0.95);
  border-color: rgb(184 105 94 / 0.45);
  transform: translateY(-2px);
  box-shadow: 0 4px 18px rgb(184 105 94 / 0.12);
}

.error-text {
  color: #c24133;
  margin-top: 1rem;
  font-size: 0.95rem;
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
