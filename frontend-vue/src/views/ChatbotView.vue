<script setup lang="ts">
import { nextTick, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

interface Message {
  role: 'user' | 'assistant'
  content: string
}

const messages = ref<Message[]>([
  { role: 'assistant', content: 'Halo! Saya adalah asisten AI yang siap membantu Anda. Silakan tanyakan apa saja! 😊' },
])
const inputText = ref('')
const isLoading = ref(false)
const messagesContainer = ref<HTMLElement | null>(null)

async function scrollToBottom() {
  await nextTick()
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

async function sendMessage() {
  const text = inputText.value.trim()
  if (!text || isLoading.value) return

  messages.value.push({ role: 'user', content: text })
  inputText.value = ''
  isLoading.value = true
  await scrollToBottom()

  try {
    const res = await fetch(`${API_URL}/chat`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem('token') || ''}`,
      },
      body: JSON.stringify({ message: text }),
    })
    const data = await res.json()
    messages.value.push({
      role: 'assistant',
      content: data.reply || data.message || 'Maaf, saya tidak bisa menjawab saat ini.',
    })
  } catch {
    messages.value.push({
      role: 'assistant',
      content: '⚠️ Tidak dapat terhubung ke server. Pastikan backend berjalan dan OpenAI API key sudah dikonfigurasi.',
    })
  } finally {
    isLoading.value = false
    await scrollToBottom()
  }
}

function handleKeydown(e: KeyboardEvent) {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    sendMessage()
  }
}
</script>

<template>
  <div class="chat-page">
    <header class="chat-header">
      <button class="back-btn" @click="router.push('/dashboard')">← Dashboard</button>
      <div class="header-center">
        <span class="bot-icon">🤖</span>
        <div>
          <h2>AI Chatbot</h2>
          <small>Powered by OpenAI GPT</small>
        </div>
      </div>
      <div class="header-spacer"></div>
    </header>

    <div class="messages-wrapper" ref="messagesContainer">
      <div class="messages">
        <div
          v-for="(msg, idx) in messages"
          :key="idx"
          :class="['message', msg.role === 'user' ? 'message-user' : 'message-assistant']"
        >
          <div class="bubble">
            <pre class="msg-text">{{ msg.content }}</pre>
          </div>
        </div>

        <div v-if="isLoading" class="message message-assistant">
          <div class="bubble typing-indicator">
            <span></span><span></span><span></span>
          </div>
        </div>
      </div>
    </div>

    <div class="input-area">
      <textarea
        v-model="inputText"
        placeholder="Ketik pesan Anda di sini... (Enter untuk kirim)"
        rows="1"
        @keydown="handleKeydown"
        :disabled="isLoading"
      ></textarea>
      <button class="send-btn" @click="sendMessage" :disabled="isLoading || !inputText.trim()">
        {{ isLoading ? '⏳' : '➤' }}
      </button>
    </div>
  </div>
</template>

<style scoped>
.chat-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f0f2f5;
}

.chat-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 0.75rem 1rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
}

.back-btn {
  background: rgba(255,255,255,0.2);
  color: white;
  border: none;
  padding: 0.4rem 0.8rem;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.85rem;
}

.back-btn:hover {
  background: rgba(255,255,255,0.3);
}

.header-center {
  display: flex;
  align-items: center;
  gap: 0.6rem;
}

.bot-icon {
  font-size: 1.8rem;
}

.header-center h2 {
  font-size: 1.1rem;
  margin: 0;
}

.header-center small {
  opacity: 0.8;
  font-size: 0.75rem;
}

.header-spacer {
  width: 80px;
}

.messages-wrapper {
  flex: 1;
  overflow-y: auto;
  padding: 1rem;
}

.messages {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.message {
  display: flex;
}

.message-user {
  justify-content: flex-end;
}

.message-assistant {
  justify-content: flex-start;
}

.bubble {
  max-width: 75%;
  padding: 0.75rem 1rem;
  border-radius: 16px;
  font-size: 0.9rem;
  line-height: 1.5;
}

.message-user .bubble {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-bottom-right-radius: 4px;
}

.message-assistant .bubble {
  background: white;
  color: #333;
  box-shadow: 0 1px 4px rgba(0,0,0,0.1);
  border-bottom-left-radius: 4px;
}

.msg-text {
  white-space: pre-wrap;
  word-break: break-word;
  font-family: inherit;
  margin: 0;
}

.typing-indicator {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 0.6rem 1rem;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  background: #aaa;
  border-radius: 50%;
  animation: bounce 1.2s infinite;
}

.typing-indicator span:nth-child(2) { animation-delay: 0.2s; }
.typing-indicator span:nth-child(3) { animation-delay: 0.4s; }

@keyframes bounce {
  0%, 80%, 100% { transform: translateY(0); }
  40% { transform: translateY(-8px); }
}

.input-area {
  background: white;
  padding: 0.75rem 1rem;
  display: flex;
  gap: 0.5rem;
  align-items: flex-end;
  box-shadow: 0 -2px 8px rgba(0,0,0,0.06);
  max-width: 100%;
}

.input-area textarea {
  flex: 1;
  border: 1.5px solid #ddd;
  border-radius: 12px;
  padding: 0.65rem 0.9rem;
  font-size: 0.9rem;
  resize: none;
  outline: none;
  font-family: inherit;
  max-height: 120px;
  overflow-y: auto;
  line-height: 1.5;
}

.input-area textarea:focus {
  border-color: #764ba2;
}

.input-area textarea:disabled {
  background: #f9f9f9;
  cursor: not-allowed;
}

.send-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  width: 44px;
  height: 44px;
  border-radius: 12px;
  cursor: pointer;
  font-size: 1.1rem;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.send-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
