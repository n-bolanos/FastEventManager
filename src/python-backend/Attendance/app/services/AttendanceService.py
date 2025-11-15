'''
Contains the actual class for operations on the Attendance model.
'''
from pydantic import BaseModel, EmailStr
from sqlalchemy import select
from Attendance.app.db.Database import get_db
from Attendance.app.models.Attendance import Attendance


class AttendanceService(BaseModel):
    '''
    Handles all the crud operations and 
    services needed for an attendance.
    '''

    name:str
    email:EmailStr
    contact_number:str
    doc_id:str
    waitlist:bool|None = None
    event_assistance_id:int

    async def confirmAttendace(self):
        '''
        Confirm attendance of an user to an event.
        '''
        async for db in get_db():
            new_attendance = Attendance(
                name=self.name,
                email=self.email,
                contact_number=self.contact_number,
                doc_id=self.doc_id,
                waitlist=self.waitlist,
                event_assistance_id=self.event_assistance_id
            )
            db.add(new_attendance)
            await db.commit()
            await db.refresh(new_attendance)
            return new_attendance
        
    @staticmethod
    async def getAttendanceByEvent(event_id:int):
        '''
        Get all attendances for an event.
        '''
        async for db in get_db():
            stmt = select(Attendance).where(Attendance.eventAssistanceID == event_id)
            result = await db.execute(stmt)
            attendance = result.scalar_one_or_none()  # retorna objeto o None
            return attendance
