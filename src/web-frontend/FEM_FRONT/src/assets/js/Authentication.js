import { useAuthStore } from '@/stores/auth';
import api_gateway from '@/service/api_gateway';

export async function checkCredentials(username, password) {
    try {
        const authStore = useAuthStore();
        const response = await api_gateway.post('/auth/login', { username, password });
        authStore.setAccessToken(response.data.accessToken);
        
        return response; 
    } catch (error) {
        console.error('Login failed:', error);
        throw error;
    }
}

export async function userRegister(name, username, email, password) {
    try {
        const res = await api_gateway.post('auth/register', {name, username, email, password});
        
        return res
    } catch (error) {
        console.error("Register error:", error);
        throw error;
    }
}