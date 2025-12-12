# Integration - Global Tests (Python / pytest)

Overview
-
This folder contains global integration tests written with `pytest` that exercise the project's HTTP APIs using `httpx`.
The tests target the gateway API at `http://localhost:8010` and depend on the backend services being available.

What the tests do
-
- `conftest.py` contains fixtures used by the tests:
	- `test_user`: generates a unique test user payload
	- `tokens_data`: waits for `GET /health` (up to ~30s) then registers and logs in to obtain tokens
	- `client`: a small sync wrapper around `httpx.AsyncClient` for test requests
	- `auth_headers`: returns an auth header set using tokens from `tokens_data`
- Individual `test_*.py` files exercise auth, events, attendance and gateway health endpoints.

Prerequisites
-
- Python 3.12+ (project `pyproject.toml` specifies `requires-python = ">=3.12"`).
- The API gateway and backend services must be running and reachable at `http://localhost:8010`.
	- The tests poll `/health` and will fail if services do not become healthy.
- Network access to any external services the backend requires (databases, Kafka, etc.), or start the full stack locally (e.g., via the repository's docker-compose manifests).

Install & run (recommended)
-
PowerShell (Windows):

```powershell
# uv is needed to use the virtual env
pip install uv

uv sync

# run all tests
uv run pytest -v
```

Troubleshooting
-
- Failing at the initial wait: ensure the gateway is up and `GET http://localhost:8010/health` returns `200`.
- Authentication failures: `tokens_data` attempts to register the test user before logging in; if the registration endpoint is flaky the login step may still succeed if the user was previously created.
- Use `print` output from the tests to inspect responses (the fixtures print status and response text on failures).
- If you prefer to run the services locally with Docker, start the required services (gateway, auth, event manager, attendance, etc.) before running the tests. The repository contains Docker compose files in `src/docker/` and per-service folders.
