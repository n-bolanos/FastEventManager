from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session

from logic.database import SessionLocal
from logic.schemas import EventCreate, EventResponse
from logic import crud

router = APIRouter(prefix="/events", tags=["Events"])


def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


@router.post("/", response_model=EventResponse)
def create_event(event: EventCreate, db: Session = Depends(get_db)):
    return crud.create_event(db, event)


@router.get("/user/{user_id}", response_model=list[EventResponse])
def get_user_events(user_id: int, db: Session = Depends(get_db)):
    return crud.get_events_by_user(db, user_id)


@router.delete("/{event_id}")
def delete_event(event_id: int, db: Session = Depends(get_db)):
    event = crud.delete_event(db, event_id)
    if not event:
        raise HTTPException(status_code=404, detail="Event does not exist")
    return {"detail": "Event deleted"}


@router.get("/{event_id}/share")
def share_event(event_id: int):
    link = f"https://miapp.com/formulario?event_id={event_id}"
    return {"share_link": link}
