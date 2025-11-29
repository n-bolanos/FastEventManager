import { ref } from 'vue'
import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || null)

  function setToken(accessToken) {
    token.value = accessToken
    localStorage.setItem('token', accessToken)
  }

  function logout() {
    token.value = null
    localStorage.removeItem('token')
  }

  return {
    token,
    setToken,
    logout
  }
});