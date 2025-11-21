import requests
import unittest
import main

class EndPointTesting(unittest.TestCase):

    def setUp(self):
        """Se ejecuta antes de cada prueba."""
        main.start_test()
    

if __name__ == '__main__':
    unittest.main()

url_post = "http://127.0.0.1:8000/attendance/confirm/?capacity=10&event_name=pinata&date=2025-02-12&location=peru&creator_id=1"
url_put = "http://127.0.0.1:8000/attendance/update/"
url_get0 = "http://127.0.0.1:8000/attendance/0"
url_get1 = "http://127.0.0.1:8000/attendance/1"
# JSON body
data = {
	"name": "Jose Carlos",
    "email": "mateo@unal.edu.co",
    "contact_number": "1",
    "doc_id": "1031648907",
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

response0 = requests.post(url_post, json=data)
#response1 = requests.post(url_post, json=data1)
#response2 = requests.post(url_post, json=data2)
#response3 = requests.get(url_get0)
#response4 = requests.get(url_get1)

# Check status and response
#print(response0)
#print("Status code:", response0.status_code)
#print("Response body:", response0.json())
#print("Status code:", response1.status_code)
#print("Response body:", response1.json())
#print("Status code:", response2.status_code)
#print("Response body:", response2.json())

#print("Response event 0", response3.json())
#print("Response event 1", response4.json())
#print(response0.json().get("attendance").get("attendanceID"))