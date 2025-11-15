from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column
from sqlalchemy import Integer, String, Boolean
from app.db.Database import Base

class Attendance(Base):
    __tablename__ = "Attendance"

    attendanceID: Mapped[int] = mapped_column(Integer, primary_key=True, index=True, autoincrement=True)
    nameAttendance: Mapped[str] = mapped_column(String(100), nullable=False)
    emailAttendance: Mapped[str] = mapped_column(String(120), unique=True, nullable=False)
    contactNumber: Mapped[str] = mapped_column(String(120), unique=True, nullable=False)
    documentID: Mapped[int] = mapped_column(Integer, nullable=False, index=True)
    waitlist: Mapped[bool] = mapped_column(Boolean, nullable=False)
    eventAssistanceID: Mapped[int] = mapped_column(Integer, nullable=False, index=True)

    def __repr__(self) -> str:
        return f"<Attendance(id={self.attendance_id} name={self.name!r} email={self.email!r})>"
    