import requests

url_post = "http://127.0.0.1:8000/attendance/confirm/?capacity=10"
url_put = "http://127.0.0.1:8000/attendance/update/"
url_get0 = "http://127.0.0.1:8000/attendance/0"
url_get1 = "http://127.0.0.1:8000/attendance/1"
# JSON body
data = {
	"name": "Jorde",
    "email": "mateo@unal.edu.co",
    "contact_number": "1",
    "doc_id": "1031648906",
    "event_assistance_id" :1

}
data1 = {
	"name": "Jorge",
    "email": "sfetecua@unal.edu.co",
    "contact_number": "3107799009",
    "doc_id": "1031648907",
    "event_assistance_id" :1

}
data2 = {
	"name": "Daniel",
    "email": "sfetecua@unal.edu.co",
    "contact_number": "1031648907",
    "doc_id": "1031648908",
    "event_assistance_id" :1

}

response0 = requests.put(url_put, json=data)
#response1 = requests.post(url_post, json=data1)
#response2 = requests.post(url_post, json=data2)
#response3 = requests.get(url_get0)
#response4 = requests.get(url_get1)

# Check status and response
print(response0)
#print("Status code:", response0.status_code)
#print("Response body:", response0.json())
#print("Status code:", response1.status_code)
#print("Response body:", response1.json())
#print("Status code:", response2.status_code)
#print("Response body:", response2.json())

#print("Response event 0", response3.json())
#print("Response event 1", response4.json())