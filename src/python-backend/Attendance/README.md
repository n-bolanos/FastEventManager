# Attendance microservice

This service handle all the attedance confirmation and status related issues.

## Endpoints:


- **GET** `/` — Status `200`
	- **Description:** Check service status.
	- **Response:** `{"status": "ok"}`

- **POST** `/confirm/` — Status `201`
	- **Description:** Confirm attendance to an event. May place the attendee on the waitlist if the event is full and sends notifications via Kafka.
	- **Body (JSON Attendance):**
		- **name**: `str`
		- **email**: `EmailStr` (email address)
		- **contact_number**: `str`
		- **doc_id**: `str`
		- **waitlist**: `bool | None` (default `False`)
		- **event_assistance_id**: `int`
	- **Query parameters:**
		- `capacity` (int) — event capacity
		- `event_name` (str)
		- `date` (str)
		- `location` (str)
		- `creator_id` (int)
	- **Behavior:**
		- If `capacity < current + 1` the attendance is marked as waitlist and a `WaitList` message is sent.
		- If there is space, a confirmation is created and a `Confirmation` message is sent; when the confirmed attendee fills the last slot the event owner is notified with a `Capacity reached` message.
	- **Response:** `{"attendance": <new_attendance_object>}`

- **PUT** `/update/` — Status `202`
	- **Description:** Update the data of an attendance for an event.
	- **Body (JSON Attendance):** same schema as POST `/confirm/`.
	- **Response:** `{"attendance": <updated_attendance_object>}`

- **GET** `/event/{event_id}` — Status `200`
	- **Description:** Retrieve all attendances for a given event.
	- **Path parameters:** `event_id` (int)
	- **Response:** `{"data": <attendance_list>}`

- **GET** `/check/document/{document_id}/event/{event_id}` — Status `200`
	- **Description:** Check whether a user (by document ID) has confirmed attendance to an event.
	- **Path parameters:**
		- `document_id` (str)
		- `event_id` (int)
	- **Response:** `{"response": False}` if not confirmed, otherwise `{"response": <attendance_record>}`

- **PUT** `/waitlist/switch/id/{document}/event/{event_id}` — Status `202`
	- **Description:** Switch the waitlist status for a user on a given event (e.g., promote from waitlist) and send a promotion notification.
	- **Path parameters:**
		- `document` (str) — user document id
		- `event_id` (int)
	- **Query parameters:**
		- `event_name` (str)
		- `date` (str)
		- `location` (str)
	- **Behavior:** If promotion occurs a `WaitListPromotion` message is sent to the attendee.
	- **Response:** `{"attendance": <attendance_after_switch>}`

**Notes**
- The endpoints use the `AttendanceService` model for request bodies; ensure you send the expected JSON schema.
- Notifications are sent using Kafka message types: `Confirmation`, `Capacity`, `WaitList`, `WaitListPromotion`.
