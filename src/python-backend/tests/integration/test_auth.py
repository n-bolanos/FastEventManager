import pytest

@pytest.mark.asyncio
async def test_login_flow(client, test_user):
    r = await client.post("/auth/login", json={
        "email": test_user["email"],
        "password": test_user["password"]
    })

    assert r.status_code == 200
    assert "accessToken" in r.json()
    assert "refreshToken" in r.json()


@pytest.mark.asyncio
async def test_refresh_token(client, tokens):
    r = await client.post("/auth/refresh", headers={
        "X-Refresh-Token": tokens["refresh"]
    })

    assert r.status_code == 200
    assert "token" in r.json()
