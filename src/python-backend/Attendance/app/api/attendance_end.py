from fastapi import APIRouter, status
from app.services.AttendanceService import AttendanceService
router = APIRouter()

@router.get("/", status_code=status.HTTP_200_OK)
def check_status():
    return {"status": "ok"}

@router.post("/confirm/", status_code=status.HTTP_201_CREATED)
async def confirm_attendance(confirmation: AttendanceService, capacity: int):
    '''
    Endpoint to confirm attendance to an event.

    Fill the body with a Attendance JSON containing:
        name:str
        email:EmailStr
        contact_number:str
        doc_id:str
        waitlist:bool|None = False
        event_assistance_id:int
    
    Remember to give the capacity of the event using the query parameter capacity.
    '''
    if capacity > AttendanceService.getNumberOfAttendances(confirmation.event_assistance_id):
        confirmation.waitlist = True
    new_attendance = await confirmation.confirmAttendace()
    return {"attendance": new_attendance}

@router.put("/update/", status_code=status.HTTP_202_ACCEPTED)
async def update_attendance(confirmation: AttendanceService, capacity: int):
    '''
    Endpoint to update the data of an attendance to an event.
    '''
    if capacity > AttendanceService.getNumberOfAttendances(confirmation.event_assistance_id):
        confirmation.waitlist = True
    new_attendance = await confirmation.confirmAttendace()
    return {"attendance": new_attendance}

@router.get("/{event_id}", status_code=status.HTTP_200_OK)
async def get_attendances(event_id: int):
    '''
    Endpoint to retrive the attendances of an event.
    '''
    attendance = await AttendanceService.getAttendanceByEvent(event_id)
    return {"data": attendance}

@router.get("/{document_id}/", status_code=status.HTTP_200_OK)
async def check_if_confirmed(event_id: int, document_id: str):
    '''
    Check if a user with document (document_id) has confirmed 
    his/her attendance to the event(event_id)
    '''
    attendance = await AttendanceService.getAttendanceByID(document_id, event_id)
    if attendance is None:
        return {"response": False}
    return {"response": attendance}
