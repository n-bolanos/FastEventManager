import { useToast } from 'vue-toastification'
import api_gateway from '@/service/api_gateway';

const toast = useToast();

export async function create_event(event_data){

    try {
        const res = await api_gateway.post("events/", req);
        toast.success("El evento ha sido creado!\n(Si no lo ves reflejado, recarga para verlo en la lista de eventos)")
        return await res.data
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}

export async function get_events(user_id){

    try {
        const res = await api_gateway.get(`/events/user/${user_id}`)
        const ans = await res.data
        return ans
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}

export async function delete_events(event_id){

    try {
        const res = await api_gateway.delete(`events/${event_id}`)
        const ans = await res.data
        toast.success("El evento ha sido eliminado!\n(Si sigue en la lista de eventos, recarga para ver los cambios reflejados en la lista de eventos)")
        return ans
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}