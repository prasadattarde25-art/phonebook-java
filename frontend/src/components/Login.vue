<template>
  <div class="login-container">
    <div class="login-card">
      <h1>📱 Phonebook</h1>
      <h2>Login</h2>

      <form @submit.prevent="login">
        <input
          v-model="username"
          type="text"
          placeholder="Username"
          required
        />

        <input
          v-model="password"
          type="password"
          placeholder="Password"
          required
        />

        <button type="submit" :disabled="loading">
          {{ loading ? 'Logging in...' : 'Login' }}
        </button>
      </form>

      <p v-if="error" class="error">
        {{ error }}
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../services/api'

const router = useRouter()

const username = ref('')
const password = ref('')
const loading = ref(false)
const error = ref('')

const login = async () => {
  error.value = ''
  loading.value = true

  try {
    const formData = new URLSearchParams()

    formData.append('username', username.value)
    formData.append('password', password.value)

    const response = await api.post(
      '/auth/login',
      formData,
      {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      }
    )

    console.log('LOGIN SUCCESS:', response.data)

    localStorage.setItem(
      'token',
      response.data.access_token
    )

    localStorage.setItem(
      'username',
      username.value
    )

    router.push('/')

  } catch (err) {

    console.error(
      'LOGIN ERROR:',
      err.response?.status
    )

    console.error(
      'LOGIN RESPONSE:',
      err.response?.data
    )

    error.value =
      err.response?.data?.detail ||
      'Invalid username or password'

  } finally {
    loading.value = false
  }
}


</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f1f5f9;
}

.login-card {
  width: 360px;
  padding: 35px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
}

h1,
h2 {
  text-align: center;
  color: #333;
}

h1 {
  margin-bottom: 10px;
}

h2 {
  margin-bottom: 25px;
}

input {
  width: 100%;
  box-sizing: border-box;
  padding: 12px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 6px;
}

button {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 6px;
  background: #007bff;
  color: white;
  font-size: 16px;
  cursor: pointer;
}

button:hover {
  background: #0056b3;
}

button:disabled {
  background: #999;
  cursor: not-allowed;
}

.error {
  margin-top: 15px;
  padding: 10px;
  background: #f8d7da;
  color: #dc3545;
  border-radius: 5px;
  text-align: center;
}
</style>