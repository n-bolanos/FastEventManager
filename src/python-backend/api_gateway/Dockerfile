FROM astral/uv:python3.12-bookworm-slim

WORKDIR /app
COPY . .

RUN uv sync

EXPOSE 8010

CMD ["uv", "run", "main.py"]