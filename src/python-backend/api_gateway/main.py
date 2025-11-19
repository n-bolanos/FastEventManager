import os
import httpx
from fastapi import FastAPI, Request, HTTPException
from starlette.responses import Response, JSONResponse
from dotenv import load_dotenv

load_dotenv()

ATTENDANCE_SVC = os.getenv("ATTENDANCE_SVC_URL")
EVENT_SVC = os.getenv("EVENT_SVC_URL")
LOGIN_SVC = os.getenv("LOGIN_SVC_URL")

app = FastAPI(title="FastEvent API Gateway")
client = httpx.AsyncClient(timeout=10.0)

async def proxy_request(method: str, target_url: str, request: Request):
    """
    Envía la petición al microservicio correspondiente.
    """
    try:
        body = await request.body()
        headers = dict(request.headers)
        headers.pop("host", None)

        response = await client.request(
            method,
            target_url,
            content=body,
            headers=headers,
            params=request.query_params
        )

        return Response(
            content=response.content,
            status_code=response.status_code,
            headers=response.headers
        )

    except httpx.RequestError as e:
        raise HTTPException(status_code=503, detail=f"Service unavailable: {str(e)}")



@app.post("/attendance/confirm")
async def confirm_attendance(request: Request):
    target = f"{ATTENDANCE_SVC}/attendance/confirm"
    return await proxy_request("POST", target, request)

@app.put("/attendance/update")
async def update_attendance(request: Request):
    target = f"{ATTENDANCE_SVC}/attendance/update"
    return await proxy_request("PUT", target, request)

@app.get("/attendance/event/{event_id}")
async def get_attendance(event_id: int, request: Request):
    target = f"{ATTENDANCE_SVC}/attendance/event/{event_id}"
    return await proxy_request("GET", target, request)

@app.get("/attendance/check/document/{doc_id}/event/{event_id}")
async def check_attendance(doc_id: str, event_id: int, request: Request):
    target = f"{ATTENDANCE_SVC}/attendance/check/document/{doc_id}/event/{event_id}"
    return await proxy_request("GET", target, request)

@app.put("/attendance/waitlist/switch/id/{doc_id}/event/{event_id}")
async def switch_waitlist(doc_id: str, event_id: int, request: Request):
    target = f"{ATTENDANCE_SVC}/attendance/waitlist/switch/id/{doc_id}/event/{event_id}"
    return await proxy_request("PUT", target, request)



@app.post("/events/")
async def create_event(request: Request):
    target = f"{EVENT_SVC}/events/"
    return await proxy_request("POST", target, request)

@app.get("/events/user/{user_id}")
async def events_by_user(user_id: int, request: Request):
    target = f"{EVENT_SVC}/events/user/{user_id}"
    return await proxy_request("GET", target, request)

@app.delete("/events/{event_id}")
async def delete_event(event_id: int, request: Request):
    target = f"{EVENT_SVC}/events/{event_id}"
    return await proxy_request("DELETE", target, request)

@app.get("/events/{event_id}/share")
async def share_event(event_id: int, request: Request):
    target = f"{EVENT_SVC}/events/{event_id}/share"
    return await proxy_request("GET", target, request)



@app.get("/health")
async def health():
    return {"gateway": "ok"}
