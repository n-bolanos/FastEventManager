import pytest

def test_create_event(client, auth_headers):
    """Test creating a new event."""
    event = {
        "name_event": "Integration Test Event",
        "description": "Event created during tests",
        "date": "2025-12-25",
        "time": "18:00:00",
        "location": "Bogotá",
        "attendance_capacity": 3,
        "creator_id": 1
    }

    r = client.post("/events/", json=event, headers=auth_headers)
    
    if r.status_code not in (200, 201):
        print(f"\n❌ Create event failed")
        print(f"Status: {r.status_code}")
        print(f"Response: {r.text}")
    
    assert r.status_code in (200, 201), f"Expected 200/201, got {r.status_code}: {r.text}"
    
    response_data = r.json()
    assert "id" in response_data or "event_id" in response_data, "Response should contain event ID"
    
    # Don't return, just assert
    event_id = response_data.get("id") or response_data.get("event_id")
    assert event_id is not None, "Event ID should not be None"


def test_get_event_by_user(client, auth_headers):
    """Test getting all events created by a specific user."""
    r = client.get("/events/user/1", headers=auth_headers)
    
    if r.status_code != 200:
        print(f"\n❌ Get events failed")
        print(f"Status: {r.status_code}")
        print(f"Response: {r.text}")
    
    assert r.status_code == 200, f"Expected 200, got {r.status_code}: {r.text}"
    
    data = r.json()
    assert isinstance(data, list), "Response should be a list of events"