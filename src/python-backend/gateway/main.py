
from fastapi import FastAPI, Request, HTTPException
import httpx
import os

app = FastAPI(title="API Gateway")

# URLs de microservicios 
AUTH_MS = os.getenv("AUTH_MS", "http://localhost:8001")       # login
EVENT_MS = os.getenv("EVENT_MS", "http://localhost:8002")     # event manager
ATTEND_MS = os.getenv("ATTEND_MS", "http://localhost:8003")   # attendance
MAIL_MS = os.getenv("MAIL_MS", "http://localhost:8004")       # mail microservice 

# mapeo basico segun prefijos
ROUTE_MAP = {
    "auth": AUTH_MS,
    "login": AUTH_MS,
    "events": EVENT_MS,
    "event": EVENT_MS,
    "attendance": ATTEND_MS,
    "confirm": ATTEND_MS,
    "mail": MAIL_MS,
    "pubsub": os.getenv("PUBSUB_BROKER", "http://localhost:9000")  # para llamadas al broker
}

# Helper: obtener destino por prefijo de path
def get_target_for_path(full_path: str):
    # extrae el primer segmento
    first = full_path.split("/")[0]
    return ROUTE_MAP.get(first)

@app.api_route("/{full_path:path}", methods=["GET", "POST", "PUT", "DELETE", "PATCH"])
async def proxy(full_path: str, request: Request):
    target = get_target_for_path(full_path)
    if not target:
        raise HTTPException(status_code=404, detail=f"No route configured for: {full_path}")

    url = f"{target}/{full_path}"

    # prepara headers y body
    headers = dict(request.headers)
    headers.pop("host", None)

    body = None
    if request.method in ("POST", "PUT", "PATCH"):
        try:
            body = await request.json()
        except Exception:
            body = await request.body()  # raw

    async with httpx.AsyncClient(timeout=15.0) as client:
        # Hace la misma llamada al microservicio destino
        resp = await client.request(request.method, url, json=body, headers=headers, params=dict(request.query_params))

    # reenvía codigo, cabeceras y contenido
    content_type = resp.headers.get("content-type", "application/json")
    return ResponseContent(resp.status_code, resp.text, content_type)

from fastapi.responses import JSONResponse, Response
def ResponseContent(status_code, text, content_type):
    # Si es JSON intenta regresar JSON
    if "application/json" in content_type:
        try:
            return JSONResponse(status_code=status_code, content=resp_json_safe(text))
        except Exception:
            return Response(content=text, status_code=status_code, media_type=content_type)
    else:
        return Response(content=text, status_code=status_code, media_type=content_type)

def resp_json_safe(text):
    import json
    return json.loads(text)
