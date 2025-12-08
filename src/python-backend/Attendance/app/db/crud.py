'''
Handles all the interactions with database using the ORM
'''
from sqlalchemy import select, update, func
from app.models.attendance_model import Attendance

class DbInteract:
    '''
    Make the databse interactions
    '''
    @staticmethod
    async def get_current_attendances(event_id, db):
        '''
        Get the exact number of attendances foe an event
        '''
        stmt = select(func.count(Attendance.attendance_id)).where(
        Attendance.event_assistance_id == event_id
        )
        result = await db.execute(stmt)
        return result.scalar_one()

    @staticmethod
    async def create_attendance(body, db):
        '''
        Create new attendance in db
        '''
        new_attendance = Attendance(**body)
        db.add(new_attendance)
        await db.commit()
        await db.refresh(new_attendance)

        return new_attendance

    @staticmethod
    async def update_attendance(document_id: str, event_id: int, body, db):
        '''
        Update an attendance in db
        '''
        stmt = (
            update(Attendance)
            .where(
                Attendance.doc_id == document_id,
                Attendance.event_assistance_id == event_id
            )
            .values(
                name=body.name,
                email=body.email,
                contact_number=body.contact_number
            )
            .execution_options(synchronize_session="fetch")
        )

        await db.execute(stmt)
        await db.commit()

        # Fetch the updated row
        result = await db.execute(
            select(Attendance).where(
                Attendance.doc_id == document_id,
                Attendance.event_assistance_id == event_id
            )
        )
        return result.scalar_one_or_none()

    @staticmethod
    async def get_attendance_by_event(event_id: int, db):
        '''
        Get attendances in an event from db
        '''
        stmt = select(Attendance).where(
            Attendance.event_assistance_id == event_id
        )
        result = await db.execute(stmt)
        return result.scalars().all()

    @staticmethod
    async def get_attendance_by_id(doc_id: str, event_id: int, db):
        '''
        Get the attendance with the combination of doc and event id from db
        '''
        stmt = select(Attendance).where(
            Attendance.doc_id == doc_id,
            Attendance.event_assistance_id == event_id
        )
        result = await db.execute(stmt)
        return result.scalars().first()

    @staticmethod
    async def switch_waitlist(doc_id: str, event_id: int, db):
        '''
        Switch the waitlist status for a person in a certain event
        '''
        # 1. Retrieve row
        stmt = select(Attendance).where(
            Attendance.doc_id == doc_id,
            Attendance.event_assistance_id == event_id
        )
        result = await db.execute(stmt)
        attendance = result.scalars().first()

        if attendance is None:
            return None

        # 2. Toggle the boolean
        attendance.waitlist = not attendance.waitlist

        # 3. Commit changes
        await db.commit()
        await db.refresh(attendance)

        # 4. Return updated row
        return attendance
