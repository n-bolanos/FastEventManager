import api_gateway from './api_gateway';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();

export async function refreshToken() {
  try {
    const response = await api_gateway.post('/auth/refresh'); // httpOnly cookie sent automatically
    authStore.setAccessToken(response.data.accessToken);
    return response.data.accessToken;
  } catch (err) {
    authStore.clear(); // logout
    window.location.href = '/login';
    throw err;
  }
}