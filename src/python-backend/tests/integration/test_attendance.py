import pytest

@pytest.mark.asyncio
async def test_confirm_attendance(client, auth_headers):
    attendance_body = {
        "name": "Laura Test",
        "email": "laura@example.com",
        "contact_number": "12345",
        "doc_id": "100200300",
        "event_assistance_id": 1,
        "waitlist": False
    }

    r = await client.post(
        "/attendance/confirm/",
        headers=auth_headers,
        json=attendance_body,
        params={
            "capacity": 3,
            "event_name": "Integration Test Event",
            "date": "2025-12-25",
            "location": "Bogotá",
            "creator_id": 1
        }
    )

    assert r.status_code in (200, 201)
    assert "attendance" in r.json()


@pytest.mark.asyncio
async def test_get_event_attendance(client, auth_headers):
    r = await client.get("/attendance/event/1", headers=auth_headers)
    assert r.status_code == 200
