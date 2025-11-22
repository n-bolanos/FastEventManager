from fastapi import FastAPI
from logic.database import Base, engine
from logic.routers import events

Base.metadata.create_all(bind=engine)

app = FastAPI(title="Event Manager Microservice")

app.include_router(events.router)

@app.get("/")
def home():
    return {"message": "Event Manager working"}
