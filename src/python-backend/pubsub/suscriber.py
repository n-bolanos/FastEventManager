
import requests
import time

BROKER_PULL = "http://localhost:9000/pull"

def process_message(msg):
    print("Procesando mensaje:", msg)
    

if __name__ == "__main__":
    print("Subscriber arrancado. Polling al broker...")
    while True:
        try:
            r = requests.get(BROKER_PULL, params={"timeout": 20})
            if r.status_code == 204:
                # no hay mensajes: esperar y volver a intentar
                time.sleep(1)
                continue
            msg = r.json()
            process_message(msg)
        except Exception as e:
            print("Error en subscriber:", e)
            time.sleep(3)
