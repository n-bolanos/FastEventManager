from fastapi import APIRouter, status
from app.services.AttendanceService import AttendanceService
router = APIRouter()

@router.get("/", status_code=status.HTTP_200_OK)
def check_status():
    return {"status": "ok"}

@router.post("/confirm/", status_code=status.HTTP_201_CREATED)
async def confirm_attendance(confirmation: AttendanceService):
    '''
    Endpoint to confirm attendance to an event.
    '''
    new_attendance = await confirmation.confirmAttendace()
    return {"attendance": new_attendance}

@router.get("/confirm/{event_id}", status_code=status.HTTP_200_OK)
async def confirm_attendance(event_id: int):
    '''
    Endpoint to retrive the attendance of an event.
    '''
    attendance = await AttendanceService.getAttendanceByEvent(event_id)
    return {"data": attendance}
