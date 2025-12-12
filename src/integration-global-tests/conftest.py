import pytest
import httpx
import time
import asyncio


@pytest.fixture(scope="session")
def test_user():
    """User data for testing."""
    timestamp = int(time.time())
    return {
        "name": "Laura Test",
        "username": f"lauratest{timestamp}",
        "email": f"laura.test.{timestamp}@example.com",
        "password": "Password123!",
        "doc_id": "100200300",
        "contact_number": "1234567890"
    }


@pytest.fixture(scope="session")
def tokens_data(test_user):
    """Get tokens synchronously."""
    
    async def _get_tokens():
        async with httpx.AsyncClient(
            base_url="http://localhost:8010",
            timeout=60.0
        ) as client:
            # Wait for services
            print("\n Waiting for services...")
            for i in range(30):
                try:
                    health = await client.get("/health")
                    if health.status_code == 200:
                        print(" Services ready")
                        break
                except:
                    if i == 29:
                        raise Exception("Services not ready")
                    await asyncio.sleep(1)
            
            # Register
            print(f"\n Registering: {test_user['email']}")
            try:
                await client.post("/auth/register", json={
                    "name": test_user["name"],
                    "username": test_user["username"],
                    "email": test_user["email"],
                    "password": test_user["password"]
                })
            except:
                pass
            
            # Login
            print("\n Logging in...")
            login_resp = await client.post("/auth/login", json={
                "identifier": test_user["email"],
                "password": test_user["password"]
            })
            
            if login_resp.status_code != 200:
                raise Exception(f"Login failed: {login_resp.text}")
            
            print(" Login successful")
            return login_resp.json()
    
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    try:
        return loop.run_until_complete(_get_tokens())
    finally:
        loop.close()


@pytest.fixture(scope="function")
def client():
    """HTTP client that runs each request in its own event loop."""
    
    class HTTPClient:
        def __init__(self):
            self.base_url = "http://localhost:8010"
            self.timeout = 60.0
        
        def _execute(self, method, url, **kwargs):
            async def _do_request():
                async with httpx.AsyncClient(
                    base_url=self.base_url,
                    timeout=self.timeout,
                    follow_redirects=True
                ) as c:
                    return await c.request(method, url, **kwargs)
            
            loop = asyncio.new_event_loop()
            asyncio.set_event_loop(loop)
            try:
                return loop.run_until_complete(_do_request())
            finally:
                loop.close()
        
        def get(self, url, **kwargs):
            return self._execute("GET", url, **kwargs)
        
        def post(self, url, **kwargs):
            return self._execute("POST", url, **kwargs)
        
        def put(self, url, **kwargs):
            return self._execute("PUT", url, **kwargs)
        
        def delete(self, url, **kwargs):
            return self._execute("DELETE", url, **kwargs)
    
    return HTTPClient()


@pytest.fixture(scope="session")
def auth_headers(tokens_data):
    """Auth headers for requests."""
    return {
        "Authorization": f"Bearer {tokens_data['accessToken']}",
        "X-Refresh-Token": tokens_data["refreshToken"]
    }