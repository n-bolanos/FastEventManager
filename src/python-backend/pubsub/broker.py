
import asyncio
from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
import uvicorn

app = FastAPI(title="Simple Pub/Sub Broker")

# Cola global
message_queue = asyncio.Queue()

@app.post("/publish")
async def publish(request: Request):
    data = await request.json()
    # data: {"type": "...", "payload": {...}}
    await message_queue.put(data)
    return JSONResponse({"status": "published", "message": data})

@app.get("/pull")
async def pull(timeout: float = 30.0):
    """
    Endpoint para que los subscribers pidan el siguiente mensaje.
    Bloquea hasta `timeout` segundos esperando un mensaje.
    Si no hay mensaje devuelve 204 No Content.
    """
    try:
        msg = await asyncio.wait_for(message_queue.get(), timeout=timeout)
        return JSONResponse(msg)
    except asyncio.TimeoutError:
        return JSONResponse({}, status_code=204)

if __name__ == "__main__":
    uvicorn.run("broker:app", host="0.0.0.0", port=9000, reload=False)
