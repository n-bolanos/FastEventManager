Go (cd) to the current folder and run the following command to start the "fem_kafka" container as well as the user_db.

docker compose up -d

The microservices that uses the kafka pub/sub connects to port 9092 and required topics are
automatically created.

The microservices that uses the authentication service connects to port 3307 and the schema is
automatically created.