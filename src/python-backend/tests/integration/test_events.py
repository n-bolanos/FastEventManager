import pytest

@pytest.mark.asyncio
async def test_create_event(client, auth_headers):
    event = {
        "title": "Integration Test Event",
        "description": "Event created during tests",
        "date": "2025-12-25",
        "location": "Bogotá",
        "capacity": 3,
        "creator_id": 1
    }

    r = await client.post("/events/", json=event, headers=auth_headers)
    assert r.status_code in (200, 201)
    assert "id" in r.json()

    event_id = r.json()["id"]
    return event_id


@pytest.mark.asyncio
async def test_get_event_by_user(client, auth_headers):
    r = await client.get("/events/user/1", headers=auth_headers)
    assert r.status_code == 200
    assert isinstance(r.json(), list)
