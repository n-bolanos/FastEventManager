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

