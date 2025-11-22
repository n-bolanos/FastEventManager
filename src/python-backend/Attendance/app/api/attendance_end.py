from fastapi import APIRouter, status
from app.services.AttendanceService import AttendanceService
from app.Kafka.kafka_producer import Confirmation, Capacity, WaitList, WaitListPromotion, send_notification
from requests import get

router = APIRouter()

@router.get("/", status_code=status.HTTP_200_OK)
def check_status():
    return {"status": "ok"}

@router.post("/confirm/", status_code=status.HTTP_201_CREATED)
async def confirm_attendance(confirmation: AttendanceService, capacity: int, event_name: str, date: str, location:str, creator_id:int):
    '''
    Endpoint to confirm attendance to an event.

    Fill the body with a Attendance JSON containing:
        name:str
        email:EmailStr
        contact_number:str
        doc_id:str
        waitlist:bool|None = False
        event_assistance_id:int
    
    Remember to give the capacity and name of the event using the query parameter capacity.
    '''
    current = await AttendanceService.getNumberOfAttendances(confirmation.event_assistance_id)
    
    if capacity < current:
        confirmation.waitlist = True

        msg_wait = WaitList(confirmation.email,
                            confirmation.name,
                            event_name)
        
        new_attendance = await confirmation.confirmAttendace()
        try:
            send_notification(msg_wait.to_dict())
        except:
            print("error sending the waitlist message")

    elif capacity > current:
        new_attendance = await confirmation.confirmAttendace()

        msg_conf = Confirmation(confirmation.email,
                                        confirmation.name,
                                        event_name,
                                        date,
                                        location)
        try:
            send_notification(msg_conf.to_dict())
        except:
            print("Error sending the message")

        if capacity == current:
            #bring the event owner's mail and name
                response = get(f"localhost:8070/auth/login/?creator_id={creator_id}")
                msg_capacity = Capacity(response.e_mail,
                                            response.name,
                                            event_name,
                                            capacity)
                try:
                    send_notification(msg_capacity.to_dict())
                except:
                    print("error sending the capacity message")
    
    return {"attendance": new_attendance}

@router.put("/update/", status_code=status.HTTP_202_ACCEPTED)
async def update_attendance(confirmation: AttendanceService):
    '''
    Endpoint to update the data of an attendance to an event.
    '''
    new_attendance = await confirmation.updateAttendance()
    return {"attendance": new_attendance}

@router.get("/event/{event_id}", status_code=status.HTTP_200_OK)
async def get_attendances(event_id: int):
    '''
    Endpoint to retrieve the attendances of an event.
    '''
    attendance = await AttendanceService.getAttendanceByEvent(event_id)
    return {"data": attendance}

@router.get("/check/document/{document_id}/event/{event_id}", status_code=status.HTTP_200_OK)
async def check_if_confirmed(event_id: int, document_id: str):
    '''
    Check if a user with document (document_id) has confirmed 
    his/her attendance to the event(event_id)
    '''
    attendance = await AttendanceService.getAttendanceByID(document_id, event_id)
    if attendance is None:
        return {"response": False}
    return {"response": attendance}

@router.put("/waitlist/switch/id/{document}/event/{event_id}", status_code=status.HTTP_202_ACCEPTED)
async def switch_waitlist_status(document: str, event_id: int, event_name:str, date:str, location:str):
    '''
    Switch the waitlist status of an user for a specified event.
    '''
    attendance = await AttendanceService.switchWaitListStatus(document, event_id)

    try:
        msj = WaitListPromotion(attendance[0].get("emailAttendance"),
                                attendance[0].get("nameAttendance"),
                                event_name,
                                date,
                                location
        )
        send_notification(msj.to_dict())
    except:
        print("error sending the message.")
    return {"attendance": attendance}
