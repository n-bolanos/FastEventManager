import { ref } from 'vue'
import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', () => {
  const access_token = ref(localStorage.getItem('access_token') || null)
  const refresh_token = ref(localStorage.getItem('refresh_token') || null)
  const user_id = ref(localStorage.getItem('user_id') || null)

  function setTokens(accessToken, refreshToken, userId) {
    access_token.value = accessToken
    localStorage.setItem('access_token', accessToken)
    refresh_token.value = refreshToken
    localStorage.setItem('refresh_token', refreshToken)
    user_id.value = userId
    localStorage.setItem('user_id', userId)
  }

  function logout() {
    access_token.value = null
    refresh_token.value = null
    user_id.value = null
    localStorage.removeItem('access_token')
    localStorage.removeItem('refresh_token')
    localStorage.removeItem('user_id')
  }

  return {
    access_token,
    refresh_token,
    user_id,
    setTokens,
    logout
  }
});