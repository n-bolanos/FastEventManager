import api_gateway from '@/service/api_gateway';


export async function confirm_event(event_id) {
    try {
        const response = await api_gateway.get(`/events/verify/${event_id}`);            
        return response.data;
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}

export async function update_or_create(person_id, event_id){
    try {
        const response = await api_gateway.get(`/attendance/check/document/${person_id}/event/${event_id}`);            
        return response.data.response;
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}

export async function confirm(body, query){
    try {
        const response = await api_gateway.post(`/attendance/confirm/`, body,
            { params: {capacity : query.capacity, event_name : query.event_name, date : query.date,
                location : query.location, creator_id : query.creator_id } }
        );            
        return response;
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}

export async function update(body){
    try {
        const response = await api_gateway.put(`/attendance/update/`, body);            
        return response;
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}

export async function get_attendances(id) {
    try {
        const response = await api_gateway.get(`/attendance/event/${id}`);            
        return response;
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}