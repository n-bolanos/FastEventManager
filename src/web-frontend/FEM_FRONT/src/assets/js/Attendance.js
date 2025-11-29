export async function confirm_event(event_id) {
    try {
        const res = await fetch(`http://localhost:8010/events/${event_id}`, {
            method: "get",
        });
            
            return await res.json()
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}

export async function update_or_create(person_id, event_id){
    try {
        const res = await fetch(
        `http://localhost:8010/attendance/check/document/${person_id}/event/${event_id}`,
        { method: "get" }
        )

        return await res.json()
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}

export async function confirm(body, query){
    try {
        const res = await fetch(
        `http://localhost:8010/attendance/confirm/${person_id}/?capacity=${query.capacity}&event_name=${query.event_name}&date=${query.date}&location=${query.location}&creator_id=${query.creator_id}`,
        { method: "post",
        body: JSON.stringify(body)
        },
        )

        return await res.json()
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}

export async function update(body){
    try {
        const res = await fetch(
        `http://localhost:8010/attendance/update/`,
        { method: "put",
        body: JSON.stringify(body)
        },
        )

        return await res.json()
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}

export async function get_attendances(id) {
    try {
        const res = await fetch(
        `http://localhost:8010/attendance/event/${id}`,
        { method: "get" }
        )

        return await res.json()
    } catch (error) {
        console.error("Fetch error:", error);
        throw error;
    }
}