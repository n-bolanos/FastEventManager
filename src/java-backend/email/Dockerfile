FROM gradle:jdk21

WORKDIR /app
COPY . .

ENV KAFKA_ADDRESS="kafka:9092"

EXPOSE 8090

CMD ["./gradlew", "bootRun"]