
# Authentication microservice

This service handles user registration, authentication (JWT issuance), refresh tokens and basic user info retrieval.

## Endpoints:


- **POST** `/auth/register` — Status `201`
		- **Description:** Register a new user account.
		- **Body (JSON RegisterRequest):**
				- **name**: `str`
				- **username**: `str`
				- **email**: `EmailStr` (email address)
				- **password**: `str`
		- **Behavior:** Validates input, hashes the password, creates a user record and returns success. If email/username already exists, returns `409`.
		- **Response:** `201 Created` (empty or user id)

- **POST** `/auth/login` — Status `200`
		- **Description:** Authenticate a user and return an access JWT and refresh token.
		- **Body (JSON LoginRequest):**
				- **identifier**: `str` (username or email)
				- **password**: `str`
		- **Behavior:** Validates credentials and returns tokens on success.
		- **Response:** `200 OK` — `{ "accessToken": "<jwt>", "refreshToken": "<jwt>", "userId": "<id>" }`

- **POST** `/auth/refresh` — Status `200`
		- **Description:** Exchange a valid refresh token for a new access token.
		- **Body (JSON RefreshRequest):**
				- **refreshToken**: `str`
		- **Response:** `200 OK` — `{ "refreshed": true, "AccessToken": "<new-jwt>" }`

- **GET** `/auth/userinfo/{user_id}` — Status `200`
		- **Description:** Retrieve basic user information for the given `user_id` (requires a valid access token).
		- **Path parameters:** `user_id` (int)
		- **Response:** `200 OK` — `{ "email": "user@example.com", "name": "Full Name" }`


**Notes**
- Authentication uses JWTs (Access + Refresh). Protect endpoints that require authentication with the `Authorization: Bearer <token>` header.
- Passwords are stored hashed (do not send plain-text passwords over unencrypted channels in production).
- Status codes:
	- `201` — Created (registration)
	- `200` — OK (login, refresh, get)
	- `401` — Unauthorized (invalid/missing token)
	- `409` — Conflict (duplicate user/email)

**Environment / Configuration**
- Provide secrets and DB connection via environment variables or `application.properties`:
	- `SPRING_DATASOURCE_URL` — JDBC connection string for user DB
	- `SPRING_DATASOURCE_USERNAME` / `SPRING_DATASOURCE_PASSWORD`
	- `JWT_SECRET` or `SPRING_JWT_SECRET` — secret key used to sign tokens
	- `JWT_EXPIRATION_MS` — access token TTL (optional)

**Examples**

- Register request example:

```
POST /auth/register
Content-Type: application/json

{
	"name": "Jane Doe",
	"username": "jdoe",
	"email": "jane@example.com",
	"password": "s3cr3t"
}
```

- Login example:

```
POST /auth/login
Content-Type: application/json

{
	"identifier": "jane@example.com",
	"password": "s3cr3t"
}
```

**Behavioral notes**
- On successful registration or password reset flows, this service may publish events to Kafka (e.g., `user.created`, `user.updated`) for other services to consume.
- Keep `JWT_SECRET` safe. In production, use a secrets manager.

If you'd like, I can also:
- Add example cURL commands for each endpoint.
- Add notes about DB migration or token revocation strategies.
