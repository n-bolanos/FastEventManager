import pytest
import httpx
import time

GATEWAY_URL = "http://localhost:8010"

@pytest.fixture(scope="session")
def client():
    return httpx.AsyncClient(base_url=GATEWAY_URL, follow_redirects=True)

@pytest.fixture(scope="session")
def test_user():
    return {
        "name": "Laura Test",
        "email": "laura.test@example.com",
        "password": "Password123!",
        "doc_id": "100200300",
        "contact_number": "1234567890"
    }

@pytest.fixture(scope="session")
async def tokens(client, test_user):
    # Register user
    await client.post("/auth/register", json={
        "name": test_user["name"],
        "email": test_user["email"],
        "password": test_user["password"],
        "doc_id": test_user["doc_id"]
    })

    # Login
    response = await client.post("/auth/login", json={
        "email": test_user["email"],
        "password": test_user["password"]
    })

    assert response.status_code == 200
    data = response.json()

    return {
        "access": data["accessToken"],
        "refresh": data["refreshToken"]
    }

@pytest.fixture()
def auth_headers(tokens):
    return {
        "Authorization": f"Bearer {tokens['access']}",
        "X-Refresh-Token": tokens["refresh"]
    }
