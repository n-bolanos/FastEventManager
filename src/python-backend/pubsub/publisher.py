
import requests
import sys
import json

BROKER = "http://localhost:9000"

def publish(event_type, payload):
    url = f"{BROKER}/publish"
    data = {"type": event_type, "payload": payload}
    r = requests.post(url, json=data)
    print("Broker response:", r.status_code, r.text)

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: python publisher.py <event_type> <json_payload>")
        sys.exit(1)
    evt = sys.argv[1]
    payload = json.loads(sys.argv[2])
    publish(evt, payload)
