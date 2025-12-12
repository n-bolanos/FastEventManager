'''
Main entry poit for the Attendance microservice
'''
from fastapi import FastAPI
from uvicorn import run
from app.api.endpoints import router as attendance_router
from contextlib import asynccontextmanager
from app.db.database import Database
import app.models.attendance_model # Load the model so it associated table can be created

def start_app():
    '''
    Start the FastAPI app
    '''

    run(
        "main:app",
        host="0.0.0.0",
        port=8000,
        reload=True
    )

@asynccontextmanager
async def startup(app: FastAPI):
    '''
    Create the database tables if needed before starting the app.
    Dispose the engine on shutDown.
    '''
    await Database.init_models()
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
