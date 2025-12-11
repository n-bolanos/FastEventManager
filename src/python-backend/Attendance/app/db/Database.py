'''
Database configuration for MySQL using SQLAlchemy Async.

This module initializes:
- Async engine
- Session factory
- Declarative base
- Startup table creation
'''

import os
from pathlib import Path
from dotenv import load_dotenv
from sqlalchemy.ext.asyncio import AsyncEngine, AsyncSession, create_async_engine
from sqlalchemy.orm import sessionmaker, DeclarativeBase

# Load .env from project root
env_path = Path(__file__).resolve().parents[2] / ".env"
load_dotenv(env_path)

# Example: mysql+aiomysql://user:password@localhost:3306/attendance_db
DATABASE_URL = os.getenv("DATABASE_URL")

if not DATABASE_URL:
    raise ValueError("DATABASE_URL is not set.")

class Base(DeclarativeBase):
    """Base class for all SQLAlchemy models"""
    pass


class Database:
    """
    Manages a single SQLAlchemy async engine and session factory.
    """

    _engine: AsyncEngine | None = None
    _session_factory: sessionmaker | None = None

    @classmethod
    def get_engine(cls) -> AsyncEngine:
        """
        Returns a singleton async engine.
        """
        if cls._engine is None:
            cls._engine = create_async_engine(
                DATABASE_URL,
                echo=False,          # Change to True for debugging
                pool_pre_ping=True,  # Avoid stale connections
            )
        return cls._engine

    @classmethod
    def get_session_factory(cls):
        """
        Returns a lazy-loaded async session factory.
        """
        if cls._session_factory is None:
            cls._session_factory = sessionmaker(
                bind=cls.get_engine(),
                class_=AsyncSession,
                expire_on_commit=False,
            )
        return cls._session_factory

    @classmethod
    async def init_models(cls):
        """
        Creates database tables at startup.
        """
        engine = cls.get_engine()
        async with engine.begin() as conn:
            await conn.run_sync(Base.metadata.create_all)

    @classmethod
    async def dispose_engine(cls):
        """
        Properly closes the engine at shutdown.
        """
        if cls._engine is not None:
            await cls._engine.dispose()
            cls._engine = None
            cls._session_factory = None


# FastAPI dependency
async def get_db():
    """
    Dependency for retrieving a SQLAlchemy async session.
    """
    SessionLocal = Database.get_session_factory()
    async with SessionLocal() as session:
        yield session
