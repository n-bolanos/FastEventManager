# Email Microservice

This service handles email composition, rendering, and delivery for all email notifications within the Fast Event Manager platform. It supports multiple email types with template-based HTML rendering and integrates with Kafka for asynchronous email processing.

## Overview

The Email service provides two primary interfaces:
- **REST API** — Synchronous email sending via HTTP, for future system robustness enhancing
- **Kafka Consumer** — Asynchronous email processing from event streams

All email types are pre-configured with HTML templates that support dynamic parameter injection.

## Endpoints

### REST API

- **POST** `/email/send` — Status `200`
	- **Description:** Send an email synchronously via HTTP request.
	- **Body (JSON EmailRequest):**
		- **to**: `str` (recipient email address)
		- **subject**: `str` (email subject line)
		- **type**: `EmailType` (enum: `SUCCESSFUL_REGISTER`, `PASSWORD_RESET`, `EVENT_CONFIRMATION`, `WAITLIST_NOTIFICATION`, `WAITLIST_PROMOTION`, `CAPACITY_REACHED`)
		- **params**: `Map<String, Object>` (template placeholder values)
	- **Response (JSON EmailResponse):**
		- **success**: `boolean` (indicates successful delivery)
		- **messageId**: `string | null` (unique identifier if successful)
		- **error**: `string | null` (error description if failed)
	- **Example Request:**
		```json
		{
			"to": "user@example.com",
			"subject": "Welcome to Fast Event Manager",
			"type": "SUCCESSFUL_REGISTER",
			"params": {
				"name": "John Doe",
				"eventName": "Tech Conference 2025"
			}
		}
		```
	- **Example Success Response:**
		```json
		{
			"success": true,
			"messageId": "550e8400-e29b-41d4-a716-446655440000",
			"error": null
		}
		```
	- **Example Error Response:**
		```json
		{
			"success": false,
			"messageId": null,
			"error": "Email sending failed: Invalid recipient address"
		}
		```

## Pub/Sub: Kafka Integration

### Consumer Topic: `email.send`

The service subscribes to the `email.send` Kafka topic to consume email requests asynchronously. This allows other microservices to trigger emails without blocking.

**Consumer Group:** `email-service`

**Message Format (JSON):**
```json
{
	"to": "attendee@example.com",
	"subject": "Event Confirmation",
	"type": "EVENT_CONFIRMATION",
	"params": {
		"eventName": "Summer Workshop",
		"date": "2025-08-15",
		"location": "Building A, Room 101",
		"attendeeName": "Jane Smith"
	}
}
```

**Behavior:**
- Messages are deserialized from JSON and converted to `EmailRequest` objects
- The email is sent via the configured SMTP server
- Parsing or processing errors are logged but do not stop the consumer
- The service continues to listen for new messages even if individual sends fail

## Email Types and Templates

| Type | Template | Use Case |
|------|----------|----------|
| `SUCCESSFUL_REGISTER` | `successful_register.html` | User successfully registers as event organizer |
| `PASSWORD_RESET` | `password_reset.html` | User requests password recovery |
| `EVENT_CONFIRMATION` | `event_confirmation.html` | Attendee confirms participation in an event |
| `WAITLIST_NOTIFICATION` | `waitlist.html` | Attendee is added to an event waitlist |
| `WAITLIST_PROMOTION` | `waitlist_promotion.html` | Waitlisted attendee is promoted to confirmed |
| `CAPACITY_REACHED` | `capacity_reached.html` | Event organizer notified that event is full |

## Template Parameters

Templates support dynamic parameter injection through the `params` map. Common parameters across templates:

- **User Information:**
	- `name` — User's full name
	- `email` — User's email address

- **Event Information:**
	- `eventName` — Name of the event
	- `date` — Event date
	- `location` — Event location
	- `creatorId` — Event organizer ID

- **Action Links:**
	- `resetLink` — Password reset URL (for `PASSWORD_RESET`)
	- `confirmationLink` — Event confirmation URL (for `EVENT_CONFIRMATION`)

## Error Handling

The service categorizes failures into three main types:

1. **Email Sending Failures** — SMTP or mail server issues
	- Example: "Email sending failed: SMTP connection timeout"

2. **Template Processing Failures** — Missing or misconfigured templates
	- Example: "Template processing failed: No template configured for type: UNKNOWN_TYPE"

3. **Unexpected Errors** — Runtime exceptions or unforeseen issues
	- Example: "Unexpected error: NullPointerException in parameter validation"

All errors are logged with full stack traces and returned in the `error` field of the response.

## Configuration

The service requires the following environment variables (via `application.properties`):

```properties
# SMTP Server Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-specific-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Kafka Configuration
spring.kafka.bootstrap-servers=kafka:9092
```

## Template Files

All HTML email templates are located in `src/main/resources/templates/`:

- `capacity_reached.html`
- `event_confirmation.html`
- `password_reset.html`
- `successful_register.html`
- `waitlist.html`
- `waitlist_promotion.html`

## Service Dependencies

- **Spring Boot Mail** — Handles SMTP email delivery
- **Spring Kafka** — Consumes messages from Kafka topics
- **Thymeleaf/Template Engine** — Renders HTML templates with dynamic parameters
- **Jackson** — Deserializes JSON messages from Kafka
- **Lombok** — Reduces boilerplate code

## Integration Notes

- The Attendance microservice publishes `Confirmation`, `Capacity Reached`, `WaitList`, and `WaitListPromotion` events which trigger corresponding emails via Kafka
- The Authentication microservice publishes registration and password reset events
- Emails are sent asynchronously to prevent blocking other services
- Failed email sends are logged but do not prevent other operations; retry logic may be implemented at the producer level

## Testing

The service includes unit and integration tests:

- **Unit Tests** — Template rendering, parameter injection, error handling
- **Integration Tests** — Full email sending workflow with mock SMTP server

Run tests with:
```bash
./gradlew test
```
