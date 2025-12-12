'''
Define all the request inputs and outputs
'''
from pydantic import BaseModel, EmailStr

class AttendanceDTO(BaseModel):
    '''
    Single attendance structure
    '''
    name:str
    email:EmailStr
    contact_number:str
    doc_id:str
    waitlist:bool|None = False
    event_assistance_id:int

class AttendanceResponse(BaseModel):
    '''
    Standard attendance response
    '''
    attendance: AttendanceDTO

class AttendanceListResponse(BaseModel):
    '''
    Attendance response object for multiple attendance answers
    '''
    data: list[AttendanceDTO]


class CheckAttendanceResponse(BaseModel):
    '''
    Attendance response for checks
    '''
    response: AttendanceDTO | bool
