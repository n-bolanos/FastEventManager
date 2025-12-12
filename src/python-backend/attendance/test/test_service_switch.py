import pytest
from unittest.mock import patch, AsyncMock, MagicMock
from app.services.app_service import AttendanceService
@pytest.mark.asyncio
async def test_switch_waitlist_success():
    service = AttendanceService()
    db = MagicMock()

    fake_row = MagicMock(email="a@mail.com", name="A")

    with patch("app.services.app_service.DbInteract.switch_waitlist", AsyncMock(return_value=fake_row)), \
         patch("app.services.app_service.send_notification") as mock_notify:

        result = await service.switch_waitlist("123", 1, "Event", "2025", "Loc", db)

        assert result == fake_row
        mock_notify.assert_called()