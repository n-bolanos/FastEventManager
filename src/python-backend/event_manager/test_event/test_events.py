from fastapi.testclient import TestClient
from main import app

client = TestClient(app)


def test_home():
    response = client.get("/")
    assert response.status_code == 200
    assert response.json() == {"message": "Event Manager working"}


def test_create_event():
    event_data = {
        "name_event": "Prueba",
        "date": "2025-05-05",
        "time": "18:00",
        "location": "Auditorio",
        "attendance_capacity": 100,
        "creator_id": 1
    }

    response = client.post("/events/", json=event_data)

    assert response.status_code == 200
    data = response.json()

    assert data["name_event"] == "Prueba"
    assert data["creator_id"] == 1
    assert "event_id" in data


def test_get_events_by_user():
    response = client.get("/events/user/1")
    assert response.status_code == 200
    assert isinstance(response.json(), list)


def test_delete_event(client):
    event_data = {
        "name_event": "Test Event",
        "date": "2025-01-10",
        "time": "14:00",
        "location": "Theater",
        "attendance_capacity": 50,
        "creator_id": 5
    }

    create_response = client.post("/events/", json=event_data)
    assert create_response.status_code == 200

    event_id = create_response.json()["event_id"]

    delete_response = client.delete(f"/events/{event_id}")
    assert delete_response.status_code == 200
    assert delete_response.json() == {"detail": "Event deleted"}

    get_response = client.get("/events/user/5")
    events = get_response.json()
    assert all(ev["event_id"] != event_id for ev in events)


