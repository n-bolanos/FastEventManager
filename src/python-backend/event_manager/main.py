from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

DATABASE_URL = "sqlite:///./events.db"

engine = create_engine(
    DATABASE_URL, connect_args={"check_same_thread": False}
)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()


class Event(Base):
    __tablename__ = "events"

    event_id = Column(Integer, primary_key=True, index=True)
    name_event = Column(String, nullable=False)
    date = Column(String, nullable=False)
    time = Column(String, nullable=False)
    location = Column(String)
    attendance_capacity = Column(Integer)
    confirmed_attendance = Column(Integer, default=0)
    creator_id = Column(Integer, nullable=False)


Base.metadata.create_all(bind=engine)


class EventCreate(BaseModel):
    name_event: str
    date: str     # format YYYY-MM-DD
    time: str     # format HH:MM
    location: str
    attendance_capacity: int
    creator_id: int


class EventResponse(BaseModel):
    event_id: int
    name_event: str
    date: str
    time: str
    location: str
    attendance_capacity: int
    confirmed_attendance: int
    creator_id: int

    class Config:
        from_attributes = True




app = FastAPI(title="Event Manager Microservice")


@app.get("/")
def home():
    return {"message": "Event Manager working"}


@app.post("/events/", response_model=EventResponse)
def create_event(event: EventCreate):
    db = SessionLocal()
    new_event = Event(**event.dict())

    db.add(new_event)
    db.commit()
    db.refresh(new_event)
    db.close()

    return new_event


@app.get("/events/user/{user_id}", response_model=List[EventResponse])
def get_events_by_user(user_id: int):
    db = SessionLocal()
    events = db.query(Event).filter(Event.creator_id == user_id).all()
    db.close()

    return events


@app.delete("/events/{event_id}")
def delete_event(event_id: int):
    db = SessionLocal()
    event = db.query(Event).filter(Event.event_id == event_id).first()

    if not event:
        db.close()
        raise HTTPException(status_code=404, detail="Event does not exist")

    db.delete(event)
    db.commit()
    db.close()



@app.get("/events/{event_id}/share")
def share_event(event_id: int):


    #TEMPORAL
    link = f"https://miapp.com/formulario?event_id={event_id}"
    return {"share_link": link}
