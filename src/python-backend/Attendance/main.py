'''
Main entry poit for the Attendance microservice
'''
from fastapi import FastAPI
from uvicorn import run
from app.api.attendance_end import router as attendance_router
from contextlib import asynccontextmanager
from app.db.Database import Database
import app.models.Attendance # Load the model so it associated table can be created
import os
from dotenv import load_dotenv
from pathlib import Path

env_path = Path(__file__).parent / ".env"
load_dotenv(env_path)

DATABASE_URL = os.getenv("DATABASE_URL")

def start_test():
    '''
    Start the FastAPI app for testing.
    '''

    DATABASE_URL = os.getenv("DATABASE_TEST_URL")
    run(
        "main:app",
        host="127.0.0.1",
        port=8000,
        reload=True
    )

def start_app():
    '''
    Start the FastAPI app
    '''
    
    run(
        "main:app",
        host="127.0.0.1",
        port=8000,
        reload=True
    )

@asynccontextmanager
async def startup(app: FastAPI):
    '''
    Create the database tables if needed before starting the app.
    Dispose the engine on shutDown.
    '''
    await Database.init_models(url=DATABASE_URL)
    yield
    # Clean up the ML models and release the resources
    await Database.dispose_engine()

app = FastAPI(lifespan=startup)

app.include_router(attendance_router, prefix="/attendance", tags=["Attendance"])

@app.get("/")
def health_check():
    return {"status": "ok"}

if __name__ == "__main__":
    start_app()
