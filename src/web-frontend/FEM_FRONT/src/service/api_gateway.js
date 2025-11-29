import axios from 'axios';
import { useAuthStore } from '@/stores/auth';

const api_gateway = axios.create({
  baseURL: 'http://localhost:8010',
  withCredentials: true, 
  validateStatus: () => true,
});

api_gateway.interceptors.request.use(config => {
    const authStore = useAuthStore();
  if (authStore.accessToken) {
    config.headers.Authorization = `Bearer ${authStore.accessToken}`;
  }
  return config;
});

api_gateway.interceptors.response.use(
  response => {
    const authStore = useAuthStore();

    // detect silent refresh
    if (response.headers["access-token-refreshed"] === "true") {
      console.log("Access token silently refreshed");
      authStore.setToken(response.data?.accessToken);
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
