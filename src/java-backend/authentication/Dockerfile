FROM gradle:jdk21

WORKDIR /app

COPY . .

RUN chmod +x gradlew

EXPOSE 8070

CMD ["./gradlew", "bootRun"]
