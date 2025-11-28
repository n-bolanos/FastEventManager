FROM python:3.12

WORKDIR /app
COPY . .

RUN curl -LsSf https://astral.sh/uv/install.sh | sh
ENV PATH="/root/.local/bin:$PATH"
RUN uv sync

ENV KAFKA_ADDRESS="kafka:9092"

EXPOSE 8000

CMD ["uv", "run", "main.py"]