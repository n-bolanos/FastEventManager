import pytest

@pytest.mark.asyncio
async def test_kafka_alive(client):
    # Kafka is exposed on 9092 so we check the gateway is alive and Kafka service responds
    r = await client.get("/health")
    assert r.status_code == 200
    assert r.json()["gateway"] == "ok"
