'''
Attendance model
'''
from sqlalchemy.orm import Mapped, mapped_column
from sqlalchemy import Integer, String, Boolean, UniqueConstraint, Index
from app.db.database import Base
from app.api.dtos import AttendanceDTO


class Attendance(Base):
    '''
    Attendance database model
    '''
    __tablename__ = "attendance"

    attendance_id: Mapped[int] = mapped_column(
        Integer, primary_key=True, autoincrement=True, index=True
    )

    name: Mapped[str] = mapped_column(String(100), nullable=False)
    email: Mapped[str] = mapped_column(String(120), nullable=False, index=True)
    contact_number: Mapped[str] = mapped_column(String(20), nullable=False)
    doc_id: Mapped[str] = mapped_column(String(50), nullable=False)

    waitlist: Mapped[bool] = mapped_column(Boolean, nullable=False, default=False)

    event_assistance_id: Mapped[int] = mapped_column(Integer, nullable=False, index=True)

    __table_args__ = (
        UniqueConstraint("email", "event_assistance_id", name="uq_email_event"),
        UniqueConstraint("doc_id", "event_assistance_id", name="uq_document_event"),
        Index("idx_document_event", "doc_id", "event_assistance_id")
    )

    def __repr__(self) -> str:
        return f"<Attendance(id={self.attendance_id}, name={self.name}, email={self.email})>"

    def to_dto(self):
        '''
        Transform the orm model into an actual response DTO
        
        '''
        return AttendanceDTO(
            name=self.name,
            email=self.email,
            contact_number=self.contact_number,
            doc_id=self.doc_id,
            waitlist=self.waitlist,
            event_assistance_id=self.event_assistance_id
        )
