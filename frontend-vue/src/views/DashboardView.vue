<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

interface UserInfo {
  email: string
  name: string
  picture?: string
}

const user = ref<UserInfo | null>(null)
const allUsers = ref<UserInfo[]>([])
const loading = ref(true)
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'

async function loadAllUsers() {
  loading.value = true
  try {
    const res = await fetch(`${API_URL}/auth/users`)
    const data = await res.json()
    if (data.success) {
      allUsers.value = data.users || []
    }
  } catch {
    // silently ignore
  } finally {
    loading.value = false
  }
}

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    user.value = JSON.parse(userStr)
  } else {
    router.push('/login')
    return
  }
  loadAllUsers()
})
</script>

<template>
  <div class="page">
    <header class="topbar">
      <div class="topbar-left">
        <h2>🤖 AI Chatbot Dashboard</h2>
      </div>
      <div class="topbar-right">
        <span v-if="user" class="user-badge">
          <img v-if="user.picture" :src="user.picture" alt="avatar" class="avatar" />
          {{ user.name }}
        </span>
        <button class="btn-outline" @click="router.push('/chatbot')">💬 Chatbot</button>
        <button class="btn-danger" @click="logout">Logout</button>
      </div>
    </header>

    <main class="content">
      <div class="welcome-card card">
        <h2>Selamat datang, {{ user?.name }}! 👋</h2>
        <p>Email: <strong>{{ user?.email }}</strong></p>
        <p class="verified-badge">✅ Email terverifikasi melalui Google</p>
      </div>

      <div class="card">
        <h3>📊 Pengguna Terdaftar ({{ allUsers.length }})</h3>
        <p class="subtitle">Semua pengguna yang telah login menggunakan akun Google</p>

        <div v-if="loading" class="loading">Memuat data pengguna...</div>

        <table v-else-if="allUsers.length > 0" class="users-table">
          <thead>
            <tr>
              <th>#</th>
              <th>Nama</th>
              <th>Email</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(u, idx) in allUsers" :key="u.email">
              <td>{{ idx + 1 }}</td>
              <td>
                <img v-if="u.picture" :src="u.picture" alt="avatar" class="table-avatar" />
                {{ u.name }}
              </td>
              <td>{{ u.email }}</td>
              <td><span class="badge-verified">✔ Terverifikasi</span></td>
            </tr>
          </tbody>
        </table>

        <p v-else class="empty">Belum ada pengguna yang login.</p>
      </div>

      <div class="card chatbot-promo">
        <h3>💬 Coba Chatbot AI kami!</h3>
        <p>Tanyakan apa saja kepada asisten AI berbasis OpenAI GPT.</p>
        <button class="btn-primary" @click="router.push('/chatbot')">Buka Chatbot →</button>
      </div>
    </main>
  </div>
</template>

<style scoped>
.page {
  min-height: 100vh;
  background: #f0f2f5;
}

.topbar {
  background: white;
  padding: 0.75rem 1.5rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}

.topbar-left h2 {
  font-size: 1.2rem;
  color: #333;
}

.topbar-right {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.user-badge {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  font-size: 0.9rem;
  color: #555;
}

.avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
}

.table-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  vertical-align: middle;
  margin-right: 4px;
}

.content {
  max-width: 900px;
  margin: 2rem auto;
  padding: 0 1rem;
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.card {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.07);
}

.welcome-card h2 {
  margin-bottom: 0.4rem;
  color: #333;
}

.welcome-card p {
  color: #666;
  margin-bottom: 0.25rem;
}

.verified-badge {
  color: #38a169;
  font-size: 0.9rem;
}

.card h3 {
  margin-bottom: 0.4rem;
  color: #333;
}

.subtitle {
  color: #888;
  font-size: 0.85rem;
  margin-bottom: 1rem;
}

.loading, .empty {
  color: #888;
  text-align: center;
  padding: 1rem;
}

.users-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
}

.users-table th {
  text-align: left;
  padding: 0.6rem 0.8rem;
  background: #f7f8fa;
  color: #555;
  border-bottom: 2px solid #eee;
}

.users-table td {
  padding: 0.6rem 0.8rem;
  border-bottom: 1px solid #f0f0f0;
  color: #333;
}

.badge-verified {
  background: #f0fff4;
  color: #38a169;
  border: 1px solid #9ae6b4;
  border-radius: 20px;
  padding: 2px 8px;
  font-size: 0.78rem;
}

.chatbot-promo {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.chatbot-promo h3 {
  color: white;
  margin-bottom: 0.4rem;
}

.chatbot-promo p {
  opacity: 0.85;
  margin-bottom: 1rem;
}

.btn-primary {
  background: white;
  color: #764ba2;
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  font-size: 0.9rem;
}

.btn-primary:hover {
  opacity: 0.9;
}

.btn-outline {
  background: transparent;
  color: #764ba2;
  border: 1.5px solid #764ba2;
  padding: 0.4rem 0.9rem;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.85rem;
}

.btn-danger {
  background: #e53e3e;
  color: white;
  border: none;
  padding: 0.4rem 0.9rem;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.85rem;
}
</style>
