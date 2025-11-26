import { defineStore } from 'pinia';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    accessToken: null,
    userId: null,
  }),
  actions: {
    setAccessToken(token) {
      this.accessToken = token;
    },
    clear() {
      this.accessToken = null;
      this.userId = null;
    }
  }
});