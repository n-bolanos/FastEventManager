'''
This file contain the api routes to access all the endpoints
'''
from fastapi import APIRouter, Depends, status
from sqlalchemy.ext.asyncio import AsyncSession
from app.db.database import get_db
from app.api.dtos import AttendanceDTO, AttendanceResponse, AttendanceListResponse, CheckAttendanceResponse
from app.services.app_service import AttendanceService


router = APIRouter()

#Check the route status
@router.get("/", status_code=status.HTTP_200_OK)
def check_status():
    '''Check service status'''
    return {"status": "ok"}

#Confirmation endpoint
@router.post("/confirm/", response_model=AttendanceResponse, status_code=status.HTTP_201_CREATED)
async def confirm_attendance(
    body: AttendanceDTO,
    capacity: int,
    event_name: str,
    date: str,
    location:str, 
    creator_id:int,
    db: AsyncSession = Depends(get_db)
    ):
    '''
    Confirm the attendance to an event
    '''
    service = AttendanceService()
    new_att = await service.confirm_event(
        body=body,
        capacity=capacity,
        event_name=event_name,
        date=date,
        location=location,
        creator_id=creator_id,
        db=db
    )

    return {"attendance": new_att}

@router.put("/update/", response_model=AttendanceResponse, status_code=status.HTTP_202_ACCEPTED)
async def update_attendance(body: AttendanceDTO, db: AsyncSession = Depends(get_db)):
    '''
    Update the attendance to an event
    '''
    service = AttendanceService()
    new_att = await service.update_att(body, db)
    return {"attendance": new_att}

@router.get("/event/{event_id}", response_model=AttendanceListResponse, status_code=status.HTTP_200_OK)
async def get_attendances(event_id: int, db: AsyncSession = Depends(get_db)):
    '''
    Endpoint to retrieve the attendances of an event.
    '''
    service = AttendanceService()
    resp = await service.get_attendance_by_event(event_id, db)
    return resp

@router.get("/check/document/{document_id}/event/{event_id}", response_model=CheckAttendanceResponse, status_code=status.HTTP_200_OK)
async def check_confirmed(
    document_id: str,
    event_id: int,
    db: AsyncSession = Depends(get_db)
):
    '''
    Check if a person with a certain id is confirmed for a determined event
    '''
    service = AttendanceService()
    return await service.check_confirmed(document_id, event_id, db)

@router.put("/waitlist/switch/id/{document}/event/{event_id}", status_code=status.HTTP_202_ACCEPTED)
async def switch_waitlist_status(document: str, event_id: int, event_name:str, date:str, location:str, db: AsyncSession = Depends(get_db)):
    '''
    Switch the waitlist status of an user for a specified event.
    '''
    service = AttendanceService()
    await service.switch_waitlist(
        document, event_id, event_name, date, location, db
    )
