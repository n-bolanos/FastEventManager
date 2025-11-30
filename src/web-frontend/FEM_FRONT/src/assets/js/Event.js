import { useToast } from 'vue-toastification'
const toast = useToast();

export async function create_event(event_data){

    try {
        const res = await fetch(
        `http://localhost:8010/events/`,
        { method: "post" ,
        body:JSON.stringify(event_data)
        },
        )
        toast.success("El evento ha sido creado!\n(Si no lo ves reflejado, recarga para verlo en la lista de eventos)")
        return await res.json()
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}

export async function get_events(user_id){

    try {
        const res = await fetch(
        `http://localhost:8010/events/user/${user_id}`,
        { method: "get" }
        )
        const ans = await res.json()
        return ans
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}

export async function delete_events(event_id){

    try {
        const res = await fetch(
        `http://localhost:8010/events/${event_id}`,
        { method: "delete" }
        )
        const ans = await res.json()
        toast.success("El evento ha sido eliminado!\n(Si sigue en la lista de eventos, recarga para ver los cambios reflejados en la lista de eventos)")
        return ans
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}