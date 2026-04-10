<template>
  <div
    class="chat-page flex min-h-screen min-h-[100dvh] w-full justify-center bg-gradient-to-br from-cream via-sand to-[#E5D9CF] px-3 py-4 sm:px-4 sm:py-6"
  >
    <div class="chat-container">
      <div class="chat-header glass-panel">
        <div class="header-info">
          <h2 class="buddy-name font-serif">{{ aiRole }} Buddy</h2>
          <span class="status-indicator">
            <span class="dot"></span> Online
          </span>
        </div>
        <button type="button" class="btn-icon" aria-label="Close chat" @click="goHome">
          <X class="h-5 w-5" aria-hidden="true" />
        </button>
      </div>

      <div ref="messagesContainer" class="messages-area">
        <div v-if="messages.length === 0" class="empty-state">
          Say hello to your new {{ aiRole }} buddy!
        </div>

        <div v-for="(msg, index) in messages" :key="index" class="message-group">
          <div class="message user-message">
            {{ msg.message }}
          </div>
          <div class="message ai-message glass-panel">
            {{ msg.response }}
          </div>
        </div>

        <div v-if="isTyping" class="message ai-message typing glass-panel">
          <span class="dot-bounce"></span>
          <span class="dot-bounce"></span>
          <span class="dot-bounce"></span>
        </div>
      </div>

      <div class="chat-input-area glass-panel">
        <form class="input-form" @submit.prevent="sendMessage">
          <input
            v-model="currentInput"
            type="text"
            class="chat-input"
            placeholder="Type your message..."
            :disabled="isTyping"
            required
          />
          <button type="submit" class="btn-send" :disabled="isTyping || !currentInput.trim()">
            <Send class="h-5 w-5" aria-hidden="true" />
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { Send, X } from 'lucide-vue-next'
import axios from 'axios'

const router = useRouter()
const aiRole = ref(localStorage.getItem('aiBuddyRole') || 'Friend')
const currentInput = ref('')
const messages = ref([])
const isTyping = ref(false)
const messagesContainer = ref(null)

onMounted(async () => {
  const userId = localStorage.getItem('userId')
  if (!userId) {
    router.push('/login')
    return
  }

  try {
    const res = await axios.get(`http://localhost:8080/chat/history/${userId}`)
    messages.value = res.data || []
    scrollToBottom()
  } catch (error) {
    console.error('Failed to load chat history', error)
  }
})

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

const sendMessage = async () => {
  if (!currentInput.value.trim() || isTyping.value) return

  const userId = localStorage.getItem('userId')
  const userText = currentInput.value

  currentInput.value = ''
  isTyping.value = true
  scrollToBottom()

  try {
    const res = await axios.post('http://localhost:8080/chat/send', {
      userId: parseInt(userId),
      message: userText,
    })

    messages.value.push(res.data)
  } catch (error) {
    console.error('Send error', error)
    messages.value.push({
      message: userText,
      response: "Sorry, I couldn't reach the server right now.",
    })
  } finally {
    isTyping.value = false
    scrollToBottom()
  }
}

const goHome = () => {
  router.push('/dashboard')
}
</script>

<style scoped>
.chat-container {
  display: flex;
  width: 100%;
  max-width: 800px;
  flex-direction: column;
  height: calc(100dvh - 2rem);
}

@media (min-width: 640px) {
  .chat-container {
    height: calc(100dvh - 3rem);
  }
}

.chat-header {
  display: flex;
  flex-shrink: 0;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1rem;
  border-radius: 20px;
  padding: 1.25rem 1.5rem;
}

@media (min-width: 768px) {
  .chat-header {
    padding: 1.5rem 2rem;
  }
}

.buddy-name {
  margin-bottom: 0.25rem;
  font-size: 1.5rem;
  color: #453d37;
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.85rem;
  color: rgb(69 61 55 / 0.55);
}

.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #b8695e;
  box-shadow: 0 0 10px rgb(184 105 94 / 0.55);
}

.btn-icon {
  display: flex;
  height: 40px;
  width: 40px;
  cursor: pointer;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  border: none;
  background: rgb(69 61 55 / 0.06);
  color: #453d37;
  transition: background 0.2s;
}

.btn-icon:hover {
  background: rgb(69 61 55 / 0.12);
}

.messages-area {
  display: flex;
  min-height: 0;
  flex: 1;
  flex-direction: column;
  gap: 1.5rem;
  overflow-y: auto;
  padding: 0.5rem 0.25rem;
  scrollbar-color: rgb(215 180 174 / 0.55) transparent;
  scrollbar-width: thin;
}

.messages-area::-webkit-scrollbar {
  width: 6px;
}
.messages-area::-webkit-scrollbar-thumb {
  border-radius: 10px;
  background-color: rgb(215 180 174 / 0.55);
}

.empty-state {
  margin: auto;
  text-align: center;
  font-size: 1.1rem;
  color: rgb(69 61 55 / 0.5);
}

.message-group {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.message {
  max-width: 85%;
  padding: 1.25rem;
  font-size: 1rem;
  line-height: 1.5;
}

@media (min-width: 640px) {
  .message {
    max-width: 80%;
  }
}

.user-message {
  align-self: flex-end;
  border-radius: 1rem;
  border-bottom-right-radius: 4px;
  background: linear-gradient(135deg, #b8695e, #9d5a52);
  color: #fffaf5;
  box-shadow: 0 4px 14px rgb(184 105 94 / 0.25);
}

.ai-message {
  align-self: flex-start;
  border-bottom-left-radius: 4px;
  border-radius: 1rem;
  padding: 1.25rem;
  color: #453d37;
}

.ai-message.glass-panel {
  padding: 1.25rem;
}

.typing.ai-message {
  display: flex;
  gap: 0.25rem;
  padding: 1rem 1.5rem;
}

.dot-bounce {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #b8695e;
  animation: bounce 1.4s infinite ease-in-out both;
}
.dot-bounce:nth-child(1) {
  animation-delay: -0.32s;
}
.dot-bounce:nth-child(2) {
  animation-delay: -0.16s;
}

.chat-input-area {
  margin-top: 1rem;
  flex-shrink: 0;
  border-radius: 20px;
  padding: 1.25rem 1.5rem;
}

.input-form {
  display: flex;
  gap: 1rem;
}

.chat-input {
  flex: 1;
  border-radius: 0.75rem;
  border: 1px solid rgb(69 61 55 / 0.14);
  background: rgb(255 250 245 / 0.95);
  padding: 0.75rem 1rem;
  font-size: 1rem;
  color: #453d37;
  outline: none;
  transition:
    border-color 0.2s,
    box-shadow 0.2s;
}

.chat-input::placeholder {
  color: rgb(69 61 55 / 0.32);
}

.chat-input:focus {
  border-color: rgb(184 105 94 / 0.5);
  box-shadow: 0 0 0 2px rgb(215 180 174 / 0.3);
}

.chat-input:disabled {
  opacity: 0.65;
}

.btn-send {
  display: flex;
  height: 50px;
  width: 50px;
  flex-shrink: 0;
  cursor: pointer;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  border: none;
  background: linear-gradient(135deg, #b8695e, #d7b4ae);
  color: #fffaf5;
  transition: transform 0.2s;
}

.btn-send:hover:not(:disabled) {
  transform: translateY(-2px);
}

.btn-send:disabled {
  cursor: not-allowed;
  opacity: 0.5;
}

@keyframes bounce {
  0%,
  80%,
  100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}
</style>
