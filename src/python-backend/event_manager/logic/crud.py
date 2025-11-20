from sqlalchemy.orm import Session
from .models import Event
from .schemas import EventCreate

def create_event(db: Session, event: EventCreate):
    new_event = Event(**event.model_dump())
    db.add(new_event)
    db.commit()
    db.refresh(new_event)
    return new_event


def get_events_by_user(db: Session, user_id: int):
    return db.query(Event).filter(Event.creator_id == user_id).all()


def delete_event(db: Session, event_id: int):
    event = db.query(Event).filter(Event.event_id == event_id).first()
    if event:
        db.delete(event)
        db.commit()
    return event
