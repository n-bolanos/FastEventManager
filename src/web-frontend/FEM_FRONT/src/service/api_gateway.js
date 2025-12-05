import axios from 'axios';
import { useAuthStore } from '@/stores/auth';

const apiUrl = import.meta.env.VITE_API_GATEWAY_URL;

const api_gateway = axios.create({
  baseURL: apiUrl,
  validateStatus: () => true,
});

api_gateway.interceptors.request.use(config => {
    const authStore = useAuthStore();
  if (authStore.access_token) {
    config.headers.Authorization = `Bearer ${authStore.access_token}`;
  }

  if (authStore.refresh_token) {
    config.headers['x-refresh-token'] = authStore.refresh_token;
  }
  return config;
});

api_gateway.interceptors.response.use(
  response => {
    const authStore = useAuthStore();

    // detect silent refresh
    if (response.headers["access-token-refreshed"] === "true") {
      authStore.setTokens(response.data?.accessToken, response.data?.refreshToken);
    }
    return response
  },
  async error => {
    const authStore = useAuthStore();

    if (error.response?.status === 401 ){
      if(response.data?.code === "LOGOUT_REQUIRED"){
        authStore.logout();
      }
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api_gateway;
