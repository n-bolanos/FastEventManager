import pytest
from unittest.mock import patch, AsyncMock, MagicMock
from app.services.app_service import AttendanceService

@pytest.mark.asyncio
async def test_update_att_success():
    service = AttendanceService()

    body = MagicMock()
    db = MagicMock()

    with patch("app.services.app_service.DbInteract.update_attendance", AsyncMock(return_value="updated")):
        response = await service.update_att(body, db)
        assert response == "updated"


@pytest.mark.asyncio
async def test_update_att_not_found():
    service = AttendanceService()

    body = MagicMock()
    db = MagicMock()

    with patch("app.services.app_service.DbInteract.update_attendance", AsyncMock(return_value=None)):
        response = await service.update_att(body, db)
        assert response is None
