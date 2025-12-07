import pytest

def test_kafka_alive(client):
    """Test that the gateway and Kafka infrastructure are working."""
    r = client.get("/health")
    
    if r.status_code != 200:
        print(f"\n Health check failed")
        print(f"Status: {r.status_code}")
        print(f"Response: {r.text}")
    
    assert r.status_code == 200, f"Expected 200, got {r.status_code}: {r.text}"
    
    data = r.json()
    assert "gateway" in data
    assert data["gateway"] == "ok"