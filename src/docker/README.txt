Go (cd) to the current folder and run the following command to start the "fem_kafka" container.

docker compose up -d

The microservices that uses the kafka pub/sub connects to port 9092 and required topics are
automatically created.