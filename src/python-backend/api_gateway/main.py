import os
from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
import httpx
from dotenv import load_dotenv
from fastapi.responses import JSONResponse, Response
import json
from datetime import datetime
import jwt


load_dotenv()

#SERVICE URLS (FROM .env)

ATTENDANCE_SVC = os.getenv("ATTENDANCE_SVC_URL", "http://localhost:8000")
EVENT_SVC = os.getenv("EVENT_SVC_URL", "http://localhost:8020")
LOGIN_SVC = os.getenv("LOGIN_SVC_URL", "http://localhost:8070")
FRONT = os.getenv("FRONT_URL", "http://localhost:8050")

#COOKIES
REFRESH_TOKEN_COOKIE = os.getenv("REFRESH_TOKEN_COOKIE_NAME", "refreshToken")

#FASTAPI APP

app = FastAPI(title="API Gateway")

app.add_middleware(
    CORSMiddleware,
    allow_origins=[FRONT],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# HELPERS

def is_token_expired(token: str) -> bool:
    """Check if JWT token is expired."""
    try:
        payload = jwt.decode(token)
        exp = payload.get("exp")
        return exp * 1000 < datetime.now().timestamp() * 1000
    except:
        return True
    
async def refresh_access_token(refresh_token: str) -> str | None:
    """Call auth service to refresh the access token."""
    try:
        async with httpx.AsyncClient() as client:
            resp = await client.post(
                f"{LOGIN_SVC}/auth/refresh",
                json={"refreshToken": refresh_token}
            )
            if resp.status_code == 200:
                data = resp.json()
                return data.get("accessToken")
    except Exception as e:
        print(f"Refresh failed: {e}")
    return None

@app.middleware("http")
async def validate_and_refresh_middleware(request: Request, call_next):
    """
    Function to validate the JWT tokens before each request sent from FrontEnd.
    It also tries to auto-refresh access JWT.
    """
    
    # Public endpoints that skip auth
    public_paths = ["/auth/login", "/auth/register", "/health", "/auth/userinfo", "/auth/logout"]
    if request.url.path in public_paths:
        return await call_next(request)
    

    refresh_token = request.cookies.get(REFRESH_TOKEN_COOKIE)
    if not refresh_token or is_token_expired(refresh_token):
        return JSONResponse(
                {"detail": "Invalid refresh token", "code": "LOGOUT_REQUIRED"},
                status_code=401)
    
    access_token = None
    jwt_refreshed = False
    auth_header = request.headers.get("Authorization", "")
    
    if auth_header.startswith("Bearer "):
        access_token = auth_header.split(" ")[1]
    
    if not access_token:
        return JSONResponse({"detail": "Unauthorized"}, status_code=401)
    

    if is_token_expired(access_token): # Second try to refresh Access JWT (Non-expired)
        access_token = await refresh_access_token(refresh_token)
        jwt_refreshed = True
    
    if not access_token:
        return JSONResponse({"detail": "Unauthorized"}, status_code=401)
       
    request.headers.__dict__["authorization"] = f"Bearer {access_token}"
    
    response = await call_next(request)
    response.headers["Access-Token-Refreshed"] = "true" if jwt_refreshed else "false"
    return response
    

#GENERIC PROXY FUNCTION

async def proxy_request(method: str, url: str, request: Request):
    async with httpx.AsyncClient() as client:
        body = await request.json() if method in ["POST", "PUT"] else None

        response = await client.request(
            method,
            url,
            json=body,
            params=request.query_params
        )

        try:
            data = response.json()
            return JSONResponse(content=data, status_code=response.status_code)
        except json.JSONDecodeError as e:
            # Aquí manejamos respuestas vacías
            print(e)
            return Response(content=response.text, status_code=response.status_code)

#ATTENDANCE ROUTES

@app.post("/attendance/confirm")
async def attendance_confirm(request: Request):
    return await proxy_request("POST", f"{ATTENDANCE_SVC}/attendance/confirm", request)

@app.put("/attendance/update")
async def attendance_update(request: Request):
    return await proxy_request("PUT", f"{ATTENDANCE_SVC}/attendance/update", request)

@app.get("/attendance/event/{event_id}")
async def attendance_get(event_id: int, request: Request):
    url = f"{ATTENDANCE_SVC}/attendance/event/{event_id}"
    return await proxy_request("GET", url, request)

@app.get("/attendance/check/document/{document_id}/event/{event_id}")
async def attendance_check(document_id: str, event_id: int, request: Request):
    url = f"{ATTENDANCE_SVC}/attendance/check/document/{document_id}/event/{event_id}"
    return await proxy_request("GET", url, request)

@app.put("/attendance/waitlist/switch/id/{document}/event/{event_id}")
async def attendance_waitlist(document: str, event_id: int, request: Request):
    url = f"{ATTENDANCE_SVC}/attendance/waitlist/switch/id/{document}/event/{event_id}"
    return await proxy_request("PUT", url, request)

#EVENT MANAGER ROUTES

@app.post("/events/")
async def create_event(request: Request):
    return await proxy_request("POST", f"{EVENT_SVC}/events/", request)

@app.get("/events/user/{user_id}")
async def events_by_user(user_id: int, request: Request):
    return await proxy_request("GET", f"{EVENT_SVC}/events/user/{user_id}", request)

@app.delete("/events/{event_id}")
async def delete_event(event_id: int, request: Request):
    return await proxy_request("DELETE", f"{EVENT_SVC}/events/{event_id}", request)

@app.get("/events/{event_id}/share")
async def share_event(event_id: int, request: Request):
    return await proxy_request("GET", f"{EVENT_SVC}/events/{event_id}/share", request)

@app.get("/events/{event_id}")
async def get_event(event_id: int, request: Request):
    return await proxy_request("GET", f"{EVENT_SVC}/events/{event_id}", request)


#LOGIN ROUTES

@app.post("/auth/register")
async def auth_register(request: Request):
    return await proxy_request("POST", f"{LOGIN_SVC}/auth/register", request)

@app.post("/auth/login")
async def auth_login(request: Request):
    auth_resp = await proxy_request("POST", f"{LOGIN_SVC}/auth/login", request)

    if auth_resp.status_code != 200:
        return auth_resp
    
    data = json.loads(auth_resp.body.decode("utf-8"))
    access_token = data.get("accessToken")
    refresh_token = data.get("refreshToken")

    response = JSONResponse(
    content={"accessToken": access_token},
    status_code=auth_resp.status_code
    )

    # Set refresh token in httpOnly cookie
    response.set_cookie(
        key=REFRESH_TOKEN_COOKIE,
        value=refresh_token,
        httponly=True,
        secure=False,
        samesite="lax",
        path="/auth",
        max_age=24*60*60
    )

    return response

@app.get("/auth/logout")
async def auth_logout():
    """Clear refresh token cookie."""
    response = JSONResponse({"detail": "Logged out"})
    response.delete_cookie(key=REFRESH_TOKEN_COOKIE, path="/auth", samesite="lax")
    return response

@app.post("/auth/refresh")
async def auth_refresh(request: Request):
    """Refresh access token using httpOnly refresh token."""
    refresh_token = request.cookies.get(REFRESH_TOKEN_COOKIE)
    
    if not refresh_token:
        return JSONResponse({"detail": "No refresh token"}, status_code=401)
    
    async with httpx.AsyncClient() as client:
        resp = await client.post(
            f"{LOGIN_SVC}/auth/refresh",
            json={"refreshToken": refresh_token}
        )
    
    if resp.status_code != 200:
        return JSONResponse({"detail": "Refresh failed"}, status_code=401)
    
    data = resp.json()
    new_access_token = data.get("accessToken")
    
    return JSONResponse({"token": new_access_token, "code": "SUCCESFUL_REFRESH"}, status_code=200)

@app.post("/auth/userinfo")
async def auth_userinfo(request: Request):
    return await proxy_request("GET", f"{LOGIN_SVC}/auth/userinfo", request)

# HEALTH CHECK

@app.get("/health")
def health():
    return {"gateway": "ok"}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8010)
