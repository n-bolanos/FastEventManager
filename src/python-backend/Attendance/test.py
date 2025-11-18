import requests

url = "http://127.0.0.1:8000/attendance/confirm/"

# JSON body
data = {
	"name": "Santiago",
    "email": "sfetecua@unal.edu.co",
    "contact_number": "3107799008",
    "doc_id": "1031648907",
    "event_assistance_id" :0

}

# Make POST request
response = requests.post(url, json=data)

# Check status and response
print("Status code:", response.status_code)
print("Response body:", response.json())