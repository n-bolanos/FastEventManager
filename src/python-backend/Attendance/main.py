from fastapi import FastAPI
from app.api.attendance import router as attendance_router

app = FastAPI()

app.include_router(attendance_router, prefix="/users", tags=["Users"])

@app.get("/")
def health_check():
    return {"status": "ok"}
