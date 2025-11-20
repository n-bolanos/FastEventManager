from pydantic import BaseModel

class EventCreate(BaseModel):
    name_event: str
    date: str   #format YYYY-MM-DD
    time: str   #format HH:MM
    location: str
    attendance_capacity: int
    creator_id: int


class EventResponse(BaseModel):
    event_id: int
    name_event: str
    date: str   #format YYYY-MM-DD
    time: str   #format HH:MM
    location: str
    attendance_capacity: int
    confirmed_attendance: int
    creator_id: int

    class Config:
        from_attributes = True
