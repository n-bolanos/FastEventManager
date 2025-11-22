from sqlalchemy import Column, Integer, String
from .database import Base

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
