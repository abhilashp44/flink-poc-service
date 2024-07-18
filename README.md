
# Spring Boot Kafka Routing Application

This is a Spring Boot application that demonstrates how to route messages to different Kafka topics based on the payload. Specifically, it checks the `id` field in the payload and routes the message to either `dest_even` or `dest_odd` topic depending on whether the `id` is even or odd.

## Features

- Produce messages to a Kafka topic.
- Consume messages from a Kafka topic.
- Route messages based on the payload.

## Prerequisites

- Java 17
- Docker and Docker Compose
- Maven

## Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd <repository-directory>
```

### Configure Kafka

Make sure Kafka is running. Follow the steps below to start Kafka using Docker and Docker Compose.

1. Navigate to the resources directory:

    ```bash
    cd PROJECT_DIR/src/main/resources
    ```

2. Start Kafka with Docker Compose:

    ```bash
    docker-compose up -d
    ```

3. Create Kafka topics:

    ```bash
    docker exec -it <kafka-container-id> kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic source
    docker exec -it <kafka-container-id> kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic dest_even
    docker exec -it <kafka-container-id> kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic dest_odd
    ```

Replace `<kafka-container-id>` with your actual Kafka container ID, which you can find using `docker ps`.

### Build and Run the Application

Use Maven to build the application:

```bash
mvn clean install
```

Start the application with the necessary environment variable:

```bash
JDK_JAVA_OPTIONS=--add-opens java.base/java.util=ALL-UNNAMED mvn spring-boot:run
```

### Pushing Events to Source Topic

Use the following command to push an event to the source topic:

```bash
echo '{"name": "exampleName", "id": 1}' | docker exec -i <kafka-container-id> kafka-console-producer --broker-list localhost:9092 --topic source
```

Replace `<kafka-container-id>` with your actual Kafka container ID.

### Checking Topics

Check if the event is pushed to the source topic:

```bash
docker exec -it <kafka-container-id> kafka-console-consumer --bootstrap-server localhost:9092 --topic source --from-beginning --timeout-ms 1000
```

Check the `dest_even` and `dest_odd` topics:

```bash
docker exec -it <kafka-container-id> kafka-console-consumer --bootstrap-server localhost:9092 --topic dest_even --from-beginning --timeout-ms 1000
docker exec -it <kafka-container-id> kafka-console-consumer --bootstrap-server localhost:9092 --topic dest_odd --from-beginning --timeout-ms 1000
```
