import pytest
from unittest.mock import patch, AsyncMock, MagicMock
from app.services.app_service import AttendanceService
from app.api.dtos import AttendanceDTO
@pytest.mark.asyncio
async def test_confirm_event_when_capacity_available():
    service = AttendanceService()

    body = AttendanceDTO(
        name="John",
        email="john@test.com",
        contact_number="123",
        doc_id="ABC",
        waitlist=False,
        event_assistance_id=1
    )

    db = MagicMock()

    # Mock DB
    with patch("app.services.app_service.DbInteract.get_current_attendances", AsyncMock(return_value=3)), \
         patch("app.services.app_service.DbInteract.create_attendance", AsyncMock(return_value="new_att")), \
         patch("app.services.app_service.send_notification") as mock_notify:

        result = await service.confirm_event(
            body=body,
            capacity=10,
            event_name="Party",
            date="2025-01-01",
            location="Room 1",
            creator_id=99,
            db=db
        )

        assert result == "new_att"
        assert body.waitlist is False
        mock_notify.assert_called()  # Confirmation should be sent

@pytest.mark.asyncio
async def test_confirm_event_when_full_goes_to_waitlist():
    service = AttendanceService()

    body = AttendanceDTO(
        name="Ana",
        email="ana@test.com",
        contact_number="890",
        doc_id="XYZ",
        waitlist=False,
        event_assistance_id=1
    )

    db = MagicMock()

    with patch("app.services.app_service.DbInteract.get_current_attendances", AsyncMock(return_value=10)), \
         patch("app.services.app_service.DbInteract.create_attendance", AsyncMock(return_value="att")), \
         patch("app.services.app_service.send_notification") as mock_notify:

        await service.confirm_event(
            body=body,
            capacity=10,   # full
            event_name="Party",
            date="2025-01-01",
            location="Room 1",
            creator_id=99,
            db=db
        )

        assert body.waitlist is True
        mock_notify.assert_called()

@pytest.mark.asyncio
async def test_notify_capacity_when_last_spot():
    service = AttendanceService()

    body = AttendanceDTO(
        name="Laura",
        email="laura@test.com",
        contact_number="111",
        doc_id="CC1",
        waitlist=False,
        event_assistance_id=1
    )

    db = MagicMock()

    with patch("app.services.app_service.DbInteract.get_current_attendances", AsyncMock(return_value=8)), \
         patch("app.services.app_service.DbInteract.create_attendance", AsyncMock(return_value="att")), \
         patch("app.services.app_service.send_notification") as notify, \
         patch("app.services.app_service.get", return_value=MagicMock(email="owner@test.com", name="Owner")):

        await service.confirm_event(
            body=body,
            capacity=9,
            event_name="Party",
            date="2025-01-01",
            location="R1",
            creator_id=5,
            db=db
        )

        assert notify.call_count == 2  # 1 confirm + 1 capacity
