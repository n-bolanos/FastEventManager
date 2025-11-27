from fastapi import FastAPI
from uvicorn import run
from logic.database import Base, engine
from logic.routers import events

Base.metadata.create_all(bind=engine)

app = FastAPI(title="Event Manager Microservice")

app.include_router(events.router)

@app.get("/")
def home():
    return {"message": "Event Manager working"}

if __name__ == "__main__":
    run("main:app", port=8020, reload=True)