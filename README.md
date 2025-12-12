# FastEventManager
### Overview
FastEventManager (or just FEM) is a full-stack microservices application for event and attendance management with user authentication. It consists of a **Vue.js** frontend, **Python** API Gateway, **Java** authentication service, and **Python** microservices for events and attendance tracking.

### Key Features
- ✅ User registration & JWT authentication
- ✅ Event creation & management
- ✅ Attendance tracking with document validation
- ✅ Event sharing & waitlist management
- ✅ Kafka-based async email notifications

### Prerequisites
- Docker & Docker Compose
- Node.js 16+ (for frontend development)
- Java 17+ (for auth service development)
- Python 3.10+ (for Python services development)

## Quick Start
### Start Infrastructure & Services
```
   cd src/docker
   docker compose --project-name "fem" up
```
Executing the lines above will start:
- **MySQL** (port 3306) — user database
- **Kafka** (port 9092) — event messaging
- **All microservices** in independent containers

### Stopping Services
Run either of the next commands to stop the execution of the app.
```
docker-compose down  # keeps volumes
docker-compose down -v  # removes volumes too
```

### Service Ports

|Service | Port  | Type |
|--------|-------|------|
|Frontend  | 8050 | Vue.js |
|API Gateway | 8010 | FastAPI |
|Authentication | 8070 | Spring Boot |
|Event Manager | 8020 | FastAPI |
|Attendance | 8000 | FastAPI |
|MySQL | 3306 | Database |
|Kafka | 9092 | Message Broker |

### General Structure of the code
```
src/
├── docker/              # Docker Compose orchestration
├── java-backend/        # Spring Boot authentication and email services
├── python-backend/      # FastAPI services (gateway, events, attendance)
└── web-frontend/        # Vue.js frontend
```

### Development Notes
- **Auth tokens**: Access and refresh tokens in Authorization header
- **CORS**: Configured for http://localhost:8050 (adjust in api_gateway/.env if needed)
- **Database**: Auto-initialized via docker compose (see authentication service)

More specific information may be consulted in the project `docs` folders or in our generated wiki:

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/n-bolanos/FastEventManager)

