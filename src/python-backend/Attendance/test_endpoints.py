import requests
import unittest
import main

class TestMiModulo(unittest.TestCase):

    def setUp(self):
        """Se ejecuta antes de cada prueba."""
        # Inicializa variables, objetos, mocks, etc.
        self.valor_inicial = 10

    def tearDown(self):
        """Se ejecuta después de cada prueba."""
        # Limpia recursos abiertos, conexiones, etc.
        pass

    def test_funcion_caso_exitoso(self):
        """Prueba un caso exitoso normal."""
        resultado = self.valor_inicial + 5  # reemplazar por tu función real
        self.assertEqual(resultado, 15)

    def test_funcion_caso_error(self):
        """Prueba cuando la función debe lanzar un error."""
        with self.assertRaises(ValueError):
            int("no es un número")  # ejemplo de error

    def test_funcion_con_valores_parametrizados(self):
        """Ejemplo de pruebas parametrizadas simples."""
        casos = [
            (1, 2, 3),
            (5, 5, 10),
            (10, 0, 10)
        ]
        for a, b, esperado in casos:
            with self.subTest(a=a, b=b):
                resultado = a + b  # reemplazar con tu función real
                self.assertEqual(resultado, esperado)

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
print(response0)
#print("Status code:", response0.status_code)
print("Response body:", response0.json())
#print("Status code:", response1.status_code)
#print("Response body:", response1.json())
#print("Status code:", response2.status_code)
#print("Response body:", response2.json())

#print("Response event 0", response3.json())
#print("Response event 1", response4.json())
print(response0.json().get("attendance").get("attendanceID"))