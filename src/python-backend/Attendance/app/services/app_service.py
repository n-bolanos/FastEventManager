'''
This file contains the core of the service, it handles all the interactions and produces the outputs
'''
from os import getenv
from requests import get
from app.db.crud import DbInteract
from app.api.dtos import AttendanceDTO, AttendanceListResponse, CheckAttendanceResponse
from app.Kafka.kafka_producer import Capacity, Confirmation, WaitList, WaitListPromotion, send_notification

class AttendanceService:
    '''
    Handles attendance functionality
    '''

    async def confirm_event(
        self,
        body: AttendanceDTO,
        capacity: int,
        event_name: str,
        date: str,
        location: str,
        creator_id: int,
        db
    ):
        '''
        Confirm a person to an event
        '''
        current_capacity = await DbInteract.get_current_attendances(
            body.event_assistance_id, db
        )

        waitlist = current_capacity + 1 > capacity
        body.waitlist = waitlist

        new_att = await DbInteract.create_attendance(body.model_dump(), db)

        if waitlist:
            self._notify_waitlist(body.email, body.name, event_name)
        else:
            self._notify_confirmation(body, event_name, date, location)

        if not waitlist and current_capacity + 1 == capacity:
            await self._notify_capacity(event_name, capacity, creator_id)

        return new_att

    async def update_att(self, body:AttendanceDTO, db):
        '''
        Update the specified event in the body
        '''
        updated = await DbInteract.update_attendance(
            document_id= body.doc_id,
            event_id= body.event_assistance_id,
            body=body,
            db=db
        )

        return updated if updated else None

    async def get_attendance_by_event(self, event_id: int, db):
        '''
        Return a list of attendances that match the given event_id
        '''
        rows = await DbInteract.get_attendance_by_event(event_id, db)

        return AttendanceListResponse(data=rows)

    async def check_confirmed(self, doc_id: str, event_id: int, db):
        '''
        Check if the person has already confirmed his attendance
        '''
        row = await DbInteract.get_attendance_by_id(doc_id, event_id, db)

        if row is None:
            return CheckAttendanceResponse(response=False)

        return CheckAttendanceResponse(response=row)

    async def switch_waitlist(
        self,
        doc_id: str,
        event_id: int,
        event_name: str,
        date: str,
        location: str,
        db
    ):
        '''
        Switch the waitlist status of an user for a specified event.
        '''
        row = await DbInteract.switch_waitlist(doc_id, event_id, db)

        if row is None:
            return None

        self._notify_promotion(row.email, row.name, event_name, date, location)

        return row

    def _notify_confirmation(self, body, event_name, date, location):
        msg = Confirmation(
            body.email,
            body.name,
            event_name,
            date,
            location
        )
        send_notification(msg.to_dict())

    async def _notify_capacity(self, event_name, capacity, creator_id):
        login_svc = getenv("LOGIN_SVC_URL")
        response = get(f"{login_svc}/auth/userinfo/?id={creator_id}", timeout=5)
        msg = Capacity(response.email, response.name, event_name, capacity)
        send_notification(msg.to_dict())

    def _notify_waitlist(self, email, name, event_name):
        msg = WaitList(email, name, event_name)
        send_notification(msg.to_dict())

    def _notify_promotion(self, email, name, event_name, date, location):
        '''
        Notify the aitlist promotion
        '''
        msg = WaitListPromotion(
            email,
            name,
            event_name,
            date,
            location
        )
        send_notification(msg.to_dict())
