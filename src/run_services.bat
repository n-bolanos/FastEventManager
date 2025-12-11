@echo off
echo ==========================
echo Starting Fast Event Manager
echo ==========================

echo.
echo --- Starting Python Services ---
echo.

start "ATTENDANCE" cmd /k "cd python-backend\attendance && uv run main.py"
start "EVENT_MANAGER" cmd /k "cd python-backend\event_manager && uv run main.py"
start "API_GATEWAY" cmd /k "cd python-backend\api_gateway && uv run main.py"

echo.
echo --- Starting Java Services ---
echo.
set "JAVA_HOME=C:\Program Files\Java\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"

start "AUTHENTICATION" cmd /k "cd java-backend\authentication && gradlew bootRun"
start "EMAIL" cmd /k "cd java-backend\email && gradlew bootRun"

echo.
echo --- Starting Frontend ---
echo.

start "FRONTEND" cmd /k "cd web-frontend\FEM_FRONT && npm install && npm run dev"

echo ==========================
echo All services started
echo ==========================
pause
