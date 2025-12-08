# Class Diagrams

This folder contains Mermaid class diagrams for each microservice in the FastEventManager project.

## Files

### `authentication.uml`
**Authentication Microservice** (Java Spring Boot)

- **Core Components:**
  - `AuthController` - REST endpoints for registration, login, token refresh, user info
  - `AuthService` - Business logic for authentication, JWT handling, email notifications
  - `JwtUtil` - JWT generation, validation, and token type extraction

- **Architecture Pattern:** Strategy Pattern
  - `CredentialStrategy` (interface) with two implementations:
    - `EmailCredentialStrategy` - Login with email + password
    - `UsernameCredentialStrategy` - Login with username + password

- **Data Layer:**
  - `User` entity - Stored user credentials and profile
  - `UserRepository` - Data access interface

- **Integration:**
  - `EmailKafkaProducer` - Publishes email events to Kafka topic

- **DTOs:**
  - `LoginRequest`, `LoginResponse`
  - `RegisterRequest`
  - `UserInfoRequest`, `UserInfoResponse`
  - `RefreshRequest`, `RefreshResponse`

### `email.uml`
**Email Microservice** (Java Spring Boot)

- **Core Components:**
  - `EmailService` - Orchestrates email composition and sending
  - `EmailController` - HTTP endpoint for direct email requests
  - `EmailKafkaListener` - Consumes email events from Kafka

- **Template System:**
  - `TemplateEngine` - Loads and renders HTML templates with placeholder replacement
  - `EmailType` enum - 6 email types (register, password reset, event confirmation, waitlist, etc.)
  - `TemplateError` - Custom exception for template failures

- **DTOs:**
  - `EmailRequest` - Email content, recipient, type, template parameters
  - `EmailResponse` - Success status, message ID, or error details

### `event_manager.uml`
**Event Manager Microservice** (FastAPI + SQLAlchemy)

- **Data Layer:**
  - `Event` - SQLAlchemy ORM model for event records
  - `Database` - SQLite connection and session management

- **API Contracts:**
  - `EventCreate` - Pydantic schema for creating events
  - `EventResponse` - Pydantic schema for returning event details

- **Business Logic:**
  - `CRUD` - Module with functional operations (create, read, delete)
    - `create_event()`
    - `get_events_by_user()`
    - `get_event_by_id()`
    - `delete_event()`

- **API Layer:**
  - `EventsRouter` - FastAPI routes using dependency injection
    - `create_event()` - Create new event
    - `get_user_events()` - Retrieve events by creator
    - `delete_event()` - Delete event by ID
    - `share_event()` - Generate shareable link
    - `get_event()` - Retrieve single event
  - `get_db()` - FastAPI dependency providing database sessions

### `attendance.uml`
**Attendance Microservice** (async SQLAlchemy + Pydantic + Kafka)

- **Data Layer:**
  - `Attendance` - SQLAlchemy ORM model for attendance records
  - `Database` - Async SQLite engine and session factory
  - `Base` - Declarative base for all ORM models

- **Service Layer:**
  - `AttendanceService` - Pydantic model with async CRUD + business logic
    - `confirmAttendance()` - Register attendance
    - `updateAttendance()` - Modify existing attendance
    - `getAttendanceByID()` - Query single attendance
    - `switchWaitListStatus()` - Toggle waitlist flag
    - `getNumberOfAttendances()` - Count attendees for event
    - `getAttendanceByEvent()` - Retrieve all attendees for event

- **Kafka Message Hierarchy:**
  - `Message` (base class) - Common fields: to, subject, type
  - `Capacity` - Event at max capacity notification
  - `Confirmation` - Attendance confirmed notification
  - `WaitList` - Added to waitlist notification
  - `WaitListPromotion` - Promoted from waitlist notification
  - `KafkaProducer` - Publishes messages to Kafka


## Viewing

The corresponding rendered graphic diagrams are present in the documentation, inside docs/Workshop-2 and inside docs/Final.
