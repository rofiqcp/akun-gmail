<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const error = ref('')
const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api'
const GOOGLE_CLIENT_ID = import.meta.env.VITE_GOOGLE_CLIENT_ID as string

function decodeJWT(token: string): Record<string, unknown> {
  try {
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join(''),
    )
    return JSON.parse(jsonPayload)
  } catch {
    return {}
  }
}

async function handleCredentialResponse(response: { credential: string }) {
  error.value = ''
  const userInfo = decodeJWT(response.credential)

  try {
    const res = await fetch(`${API_URL}/auth/google`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        token: response.credential,
        email: userInfo['email'],
        name: userInfo['name'],
        picture: userInfo['picture'],
      }),
    })
    const data = await res.json()
    if (data.success) {
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify(data.user))
      router.push('/dashboard')
    } else {
      error.value = data.message || 'Login gagal'
    }
  } catch {
    error.value = 'Tidak dapat terhubung ke server. Pastikan backend berjalan.'
  }
}

function initializeGoogleSignIn() {
  if (!GOOGLE_CLIENT_ID) {
    error.value = 'VITE_GOOGLE_CLIENT_ID belum dikonfigurasi. Salin .env.example ke .env dan isi nilainya.'
    return
  }
  const g = (window as unknown as Record<string, unknown>)['google'] as {
    accounts: { id: { initialize: (opts: unknown) => void; renderButton: (el: HTMLElement | null, opts: unknown) => void } }
  }
  g.accounts.id.initialize({
    client_id: GOOGLE_CLIENT_ID,
    callback: handleCredentialResponse,
  })
  g.accounts.id.renderButton(document.getElementById('google-signin-button'), {
    theme: 'filled_blue',
    size: 'large',
    text: 'signin_with',
    shape: 'rectangular',
    width: 280,
  })
}

onMounted(() => {
  if (localStorage.getItem('token')) {
    router.push('/dashboard')
    return
  }
  const script = document.createElement('script')
  script.src = 'https://accounts.google.com/gsi/client'
  script.async = true
  script.defer = true
  script.onload = initializeGoogleSignIn
  document.head.appendChild(script)
})
</script>

<template>
  <div class="login-page">
    <div class="login-card">
      <div class="logo-area">
        <img src="/favicon.ico" alt="Logo" width="48" height="48" />
        <h1>AI Chatbot</h1>
        <p>Masuk dengan akun Google Anda untuk mulai menggunakan chatbot AI</p>
      </div>

      <div class="divider"></div>

      <div id="google-signin-button"></div>

      <p v-if="error" class="error-msg">{{ error }}</p>

      <p class="disclaimer">
        Dengan masuk, Anda menyetujui penggunaan data akun Google Anda untuk keperluan autentikasi.
        Email Anda akan terverifikasi secara otomatis melalui akun Google.
      </p>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 1rem;
}

.login-card {
  background: white;
  border-radius: 16px;
  padding: 2.5rem 2rem;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  text-align: center;
}

.logo-area img {
  margin-bottom: 1rem;
}

.logo-area h1 {
  font-size: 1.8rem;
  font-weight: 700;
  color: #333;
  margin-bottom: 0.5rem;
}

.logo-area p {
  color: #666;
  font-size: 0.9rem;
  line-height: 1.5;
  margin-bottom: 0;
}

.divider {
  height: 1px;
  background: #eee;
  margin: 1.5rem 0;
}

#google-signin-button {
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
}

.error-msg {
  color: #e53e3e;
  font-size: 0.85rem;
  margin-top: 0.5rem;
  background: #fff5f5;
  border: 1px solid #feb2b2;
  border-radius: 6px;
  padding: 0.5rem;
}

.disclaimer {
  color: #999;
  font-size: 0.75rem;
  margin-top: 1.5rem;
  line-height: 1.5;
}
</style>
