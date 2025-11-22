import os
from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
import httpx
from dotenv import load_dotenv

load_dotenv()

#SERVICE URLS (FROM .env)

ATTENDANCE_SVC = os.getenv("ATTENDANCE_SVC_URL", "http://localhost:8000")
EVENT_SVC = os.getenv("EVENT_SVC_URL", "http://localhost:8020")
LOGIN_SVC = os.getenv("LOGIN_SVC_URL", "http://localhost:8070")
FRONT = os.getenv("FRONT_URL", "http://localhost:8050")

#FASTAPI APP

app = FastAPI(title="API Gateway")

app.add_middleware(
    CORSMiddleware,
    allow_origins=[FRONT],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

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

        return response.json()

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


#LOGIN ROUTES

@app.post("/auth/register")
async def auth_register(request: Request):
    return await proxy_request("POST", f"{LOGIN_SVC}/auth/register", request)

@app.post("/auth/login")
async def auth_login(request: Request):
    return await proxy_request("POST", f"{LOGIN_SVC}/auth/login", request)

@app.post("/auth/refresh")
async def auth_refresh(request: Request):
    return await proxy_request("POST", f"{LOGIN_SVC}/auth/refresh", request)

@app.post("/auth/userinfo")
async def auth_userinfo(request: Request):
    return await proxy_request("POST", f"{LOGIN_SVC}/auth/userinfo", request)

# HEALTH CHECK

@app.get("/health")
def health():
    return {"gateway": "ok"}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", port=8010, reload=True)

