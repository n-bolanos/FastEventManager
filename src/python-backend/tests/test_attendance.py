import pytest

def test_confirm_attendance(client, auth_headers):
    """Test confirming attendance to an event."""
    # First, create an event to attend
    event = {
        "name_event": "Attendance Test Event",
        "description": "Event for attendance testing",
        "date": "2025-12-25",
        "time": "18:00:00",
        "location": "Bogotá",
        "attendance_capacity": 3,
        "creator_id": 1
    }
    
    event_response = client.post("/events/", json=event, headers=auth_headers)
    
    if event_response.status_code not in (200, 201):
        pytest.skip(f"Cannot test attendance: event creation failed with {event_response.status_code}")
    
    event_data = event_response.json()
    event_id = event_data.get("id") or event_data.get("event_id", 1)
    
    # Now test attendance
    attendance_body = {
        "name": "Laura Test",
        "email": "laura@example.com",
        "contact_number": "12345",
        "doc_id": "100200300",
        "event_assistance_id": event_id,
        "waitlist": False
    }

    r = client.post(
        "/attendance/confirm/",
        headers=auth_headers,
        json=attendance_body,
        params={
            "capacity": 3,
            "event_name": "Attendance Test Event",
            "date": "2025-12-25",
            "time": "18:00:00",
            "location": "Bogotá",
            "creator_id": 1
        }
    )

    if r.status_code not in (200, 201):
        print(f"\n❌ Confirm attendance failed")
        print(f"Status: {r.status_code}")
        print(f"Response: {r.text}")

    assert r.status_code in (200, 201), f"Expected 200/201, got {r.status_code}: {r.text}"
    
    data = r.json()
    assert "attendance" in data or "id" in data, "Response should contain attendance data"


def test_get_event_attendance(client, auth_headers):
    """Test getting all attendees for a specific event."""
    r = client.get("/attendance/event/1", headers=auth_headers)
    
    if r.status_code != 200:
        print(f"\n❌ Get attendance failed")
        print(f"Status: {r.status_code}")
        print(f"Response: {r.text}")
    
    assert r.status_code == 200, f"Expected 200, got {r.status_code}: {r.text}"
    
    data = r.json()
    assert isinstance(data, (list, dict)), "Response should be list or dict"