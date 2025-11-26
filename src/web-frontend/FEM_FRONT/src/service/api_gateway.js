import axios from 'axios';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();

const api_gateway = axios.create({
  baseURL: 'http://localhost:8010',
  withCredentials: true, 
});

api.interceptors.request.use(config => {
  if (authStore.accessToken) {
    config.headers.Authorization = `Bearer ${authStore.accessToken}`;
  }
  return config;
});

api.interceptors.response.use(
  response => response,
  async error => {
    if (error.response?.status === 401) {
      const newToken = await refreshToken();
      error.config.headers.Authorization = `Bearer ${newToken}`;
      return api_gateway.request(error.config);
    }
    return Promise.reject(error);
  }
);

export default api_gateway;
