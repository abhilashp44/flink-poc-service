package com.flink.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flink.model.Student;
import com.flink.model.StudentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.time.Duration;

/**
 * Service implementation for receiving and processing Kafka events using Apache Flink.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class EventReceiverServiceImpl implements Serializable {

    private final ObjectMapper oMapper;
    private final KafkaSource<StudentEvent> kafkaSource;
    private final KafkaSink<Student> evenKafkaSink;
    private final KafkaSink<Student> oddKafkaSink;

    /**
     * Initializes the processing of the data stream from Kafka source to Kafka sinks.
     */
    @PostConstruct
    void processDataStream() {

        StreamExecutionEnvironment streamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        WatermarkStrategy<StudentEvent> customWaterMarkStrategy =
                WatermarkStrategy.<StudentEvent>forBoundedOutOfOrderness(Duration.ofSeconds(20))
                        .withIdleness(Duration.ofMinutes(1));

        DataStreamSource<StudentEvent> ingressDataStream =
                streamExecutionEnvironment.fromSource(kafkaSource, customWaterMarkStrategy, "kafka-signal-stream");

        ingressDataStream.map(studentEvent -> {
                    JsonNode payload = oMapper.readValue(studentEvent.getConsumerRecord().value(), JsonNode.class);
                    return new Student(payload.get("name").asText(), payload.get("id").asInt());
                }).filter(student -> student.getId() % 2 == 0)
                .returns(TypeInformation.of(Student.class)).sinkTo(evenKafkaSink);

        ingressDataStream.map(studentEvent -> {
                    JsonNode payload = oMapper.readValue(studentEvent.getConsumerRecord().value(), JsonNode.class);
                    return new Student(payload.get("name").asText(), payload.get("id").asInt());
                }).filter(student -> student.getId() % 2 != 0)
                .returns(TypeInformation.of(Student.class)).sinkTo(oddKafkaSink);

        try {
            streamExecutionEnvironment.execute("flink-poc");
        } catch (Exception e) {
            log.error("Error in running the flink job integrated with spring boot {}", e.getMessage());
        }
    }
}
