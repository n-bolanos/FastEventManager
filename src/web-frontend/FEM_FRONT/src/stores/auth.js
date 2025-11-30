import { ref } from 'vue'
import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', () => {
  const access_token = ref(localStorage.getItem('access_token') || null)
  const refresh_token = ref(localStorage.getItem('refresh_token') || null)

  function setTokens(accessToken, refreshToken) {
    access_token.value = accessToken
    localStorage.setItem('access_token', accessToken)
    refresh_token.value = refreshToken
    localStorage.setItem('refresh_token', refreshToken)
  }

  function logout() {
    access_token.value = null
    refresh_token.value = null
    localStorage.removeItem('access_token')
    localStorage.removeItem('refresh_token')
  }

  return {
    access_token,
    refresh_token,
    setTokens,
    logout
  }
});