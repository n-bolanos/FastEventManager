'''
Contains the actual class for operations on the Attendance model.
'''
from pydantic import BaseModel, EmailStr
from sqlalchemy import select, func, update
from app.db.Database import get_db
from app.models.Attendance import Attendance


class AttendanceService(BaseModel):
    '''
    Handles all the crud operations and 
    services needed for an attendance.
    '''

    name:str
    email:EmailStr
    contact_number:str
    doc_id:str
    waitlist:bool|None = False
    event_assistance_id:int

    async def confirmAttendace(self):
        '''
        Confirm attendance of an user to an event.
        '''
        async for db in get_db():
            new_attendance = Attendance(
                nameAttendance = self.name,
                emailAttendance = self.email,
                contactNumber = self.contact_number,
                documentID = self.doc_id,
                waitlist = self.waitlist,
                eventAssistanceID =self.event_assistance_id
            )
            db.add(new_attendance)
            await db.commit()
            await db.refresh(new_attendance)
            return new_attendance

    async def updateAttendance(self):
        '''
        Update the attendance of a user to an event.
        '''
        async for db in get_db():
            stmt = (
                update(Attendance)
                .where(Attendance.documentID == self.doc_id and Attendance.eventAssistanceID == self.event_assistance_id)
                .values(nameAttendance = self.name,
                emailAttendance = self.email,
                contactNumber = self.contact_number)
                )
            await db.execute(stmt)
            await db.commit()
            return await AttendanceService.getAttendanceByID(self.doc_id, self.event_assistance_id)

    @staticmethod
    async def getAttendanceByID(document:int, event_id:int):
        '''
        Get the attendance for a determined person in a specified event
        '''

        async for db in get_db():
            stmt = select(Attendance).where(Attendance.eventAssistanceID == event_id and Attendance.documentID == document)
            result = await db.execute(stmt)
            attendance = result.scalars().all()  # return objects or None
            if attendance is None:
                return []
            return [ AttendanceService.jsonify(attendance) for attendance in attendance]

    @staticmethod
    async def getNumberOfAttendances(event_id:int):
        '''
        Get the number of attendances by event.
        '''
        async for db in get_db():
            stmt = select(
                func.count(Attendance.attendanceID)
                ).where(Attendance.eventAssistanceID == event_id)
            
            result = await db.execute(stmt)
            active_count = result.scalar_one()
            return active_count
    
    @staticmethod
    async def getAttendanceByEvent(event_id:int):
        '''
        Get all attendances for an event.
        '''
        async for db in get_db():
            stmt = select(Attendance).where(Attendance.eventAssistanceID == event_id)
            result = await db.execute(stmt)
            attendance = result.scalars().all()  # return objects or None
            if attendance is None:
                return []
            return [ AttendanceService.jsonify(attendance) for attendance in attendance]

    @staticmethod
    def jsonify(attendance):
        return {
            "attendanceID": attendance.attendanceID,
            "nameAttendance": attendance.nameAttendance,
            "emailAttendance": attendance.emailAttendance,
            "contactNumber": attendance.contactNumber,
            "documentID": attendance.documentID,
            "waitlist": attendance.waitlist,
            "eventAssistanceID": attendance.eventAssistanceID
        }
