'''
Contains the Base and Database classes for the SQLALchemy setup.
'''
from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession, AsyncEngine
from sqlalchemy.orm import sessionmaker, DeclarativeBase
import os
from dotenv import load_dotenv
from pathlib import Path

env_path = Path(__file__).parent / ".env"
load_dotenv(env_path)

DATABASE_URL = os.getenv("DATABASE_URL")



class Base(DeclarativeBase):
    '''
    Base class for sqlalchemy models
    '''
    pass

class Database:
    '''
    Retrives a unique engine and session factory.
    '''
    _engine: AsyncEngine | None = None
    _session_factory = None

    @classmethod
    def get_engine(cls):
        if cls._engine is None:
            cls._engine = create_async_engine(DATABASE_URL, pool_pre_ping=True)
        return cls._engine

    @classmethod
    def get_session_factory(cls):
        if cls._session_factory is None:
            cls._session_factory = sessionmaker(
                bind=cls.get_engine(),
                class_=AsyncSession,
                expire_on_commit=False
            )
        return cls._session_factory

    @classmethod
    async def init_models(cls):
        """
        Create tables if needed
        """
        engine = cls.get_engine()
        async with engine.begin() as conn:
            # run_sync ejecuta la versión síncrona create_all bajo el hood
            await conn.run_sync(Base.metadata.create_all)

    @classmethod
    async def dispose_engine(cls):
        """
        Close the engine in shutdown.
        """
        if cls._engine is not None:
            await cls._engine.dispose()
            cls._engine = None
            cls._session_factory = None
    
# Function used by services to get a session
async def get_db():
    SessionLocal = Database.get_session_factory()
    async with SessionLocal() as session:
        yield session
