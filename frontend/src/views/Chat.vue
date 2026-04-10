<template>
  <div class="chat-container">
    <!-- Chat Header -->
    <div class="chat-header glass-panel">
      <div class="header-info">
        <h2 class="buddy-name">{{ aiRole }} Buddy</h2>
        <span class="status-indicator">
          <span class="dot"></span> Online
        </span>
      </div>
      <button @click="goHome" class="btn-icon">
        &#x2715; <!-- Close icon -->
      </button>
    </div>

    <!-- Chat Messages -->
    <div class="messages-area" ref="messagesContainer">
      <div v-if="messages.length === 0" class="empty-state">
        Say hello to your new {{ aiRole }} buddy!
      </div>
      
      <div 
        v-for="(msg, index) in messages" 
        :key="index"
        class="message-group"
      >
        <div class="message user-message glass-panel">
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

    <!-- Chat Input -->
    <div class="chat-input-area glass-panel">
      <form @submit.prevent="sendMessage" class="input-form">
        <input 
          type="text" 
          v-model="currentInput" 
          class="input-field chat-input" 
          placeholder="Type your message..." 
          :disabled="isTyping"
          required
        />
        <button type="submit" class="btn-send" :disabled="isTyping || !currentInput.trim()">
          <svg viewBox="0 0 24 24" width="24" height="24" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"><line x1="22" y1="2" x2="11" y2="13"></line><polygon points="22 2 15 22 11 13 2 9 22 2"></polygon></svg>
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
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
      message: userText
    })
    
    messages.value.push(res.data)
  } catch (error) {
    console.error('Send error', error)
    messages.value.push({
      message: userText,
      response: "Sorry, I couldn't reach the server right now."
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
  flex-direction: column;
  height: 100vh;
  width: 100%;
  max-width: 800px;
  margin: 0 auto;
  padding: 1rem;
}

@media (min-width: 768px) {
  .chat-container {
    padding: 2rem;
    height: calc(100vh - 4rem);
  }
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem 2rem;
  margin-bottom: 1rem;
  border-radius: 20px;
}

.buddy-name {
  font-size: 1.5rem;
  color: white;
  margin-bottom: 0.25rem;
}

.status-indicator {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.85rem;
  color: var(--text-secondary);
}

.dot {
  width: 8px;
  height: 8px;
  background: #10b981;
  border-radius: 50%;
  box-shadow: 0 0 10px #10b981;
}

.btn-icon {
  background: rgba(255, 255, 255, 0.1);
  border: none;
  color: white;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-icon:hover {
  background: rgba(255, 255, 255, 0.2);
}

.messages-area {
  flex: 1;
  overflow-y: auto;
  padding: 1rem 0.5rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  scrollbar-width: thin;
  scrollbar-color: var(--primary) transparent;
}

.messages-area::-webkit-scrollbar {
  width: 6px;
}
.messages-area::-webkit-scrollbar-thumb {
  background-color: var(--primary);
  border-radius: 10px;
}

.empty-state {
  margin: auto;
  color: var(--text-secondary);
  font-size: 1.1rem;
  text-align: center;
}

.message-group {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.message {
  padding: 1.25rem;
  max-width: 80%;
  line-height: 1.5;
  font-size: 1rem;
}

.user-message {
  align-self: flex-end;
  background: linear-gradient(135deg, var(--primary), var(--primary-hover));
  border-bottom-right-radius: 4px;
  color: white;
}

.ai-message {
  align-self: flex-start;
  border-bottom-left-radius: 4px;
  position: relative;
}

.typing.ai-message {
  display: flex;
  gap: 0.25rem;
  padding: 1rem 1.5rem;
}

.dot-bounce {
  width: 8px;
  height: 8px;
  background: white;
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out both;
}
.dot-bounce:nth-child(1) { animation-delay: -0.32s; }
.dot-bounce:nth-child(2) { animation-delay: -0.16s; }

.chat-input-area {
  margin-top: 1rem;
  padding: 1.5rem;
  border-radius: 20px;
}

.input-form {
  display: flex;
  gap: 1rem;
}

.chat-input {
  flex: 1;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  padding-right: 1.5rem;
}

.btn-send {
  background: linear-gradient(135deg, var(--primary), var(--secondary));
  border: none;
  border-radius: 12px;
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  transition: transform 0.2s;
  flex-shrink: 0;
}

.btn-send:hover:not(:disabled) {
  transform: translateY(-2px);
}

.btn-send:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

@keyframes bounce {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}
</style>
