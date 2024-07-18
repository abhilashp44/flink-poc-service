package com.flink.serializers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flink.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeaders;

import java.nio.charset.StandardCharsets;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Custom serialization schema for Kafka records that converts {@link Student} objects into {@link ProducerRecord}s.
 */
@Slf4j
public class CustomKafkaRecordSerializationSchema implements KafkaRecordSerializationSchema<Student> {

    private final ObjectMapper objectMapper;
    private final String topic;

    /**
     * Constructor to initialize the serialization schema with the given topic.
     *
     * @param topic the Kafka topic to which records will be sent
     */
    public CustomKafkaRecordSerializationSchema(String topic) {
        this.topic = topic;
        this.objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Initializes the serialization schema.
     *
     * @param context the initialization context
     * @param sinkContext the Kafka sink context
     * @throws Exception if an error occurs during initialization
     */
    @Override
    public void open(SerializationSchema.InitializationContext context, KafkaSinkContext sinkContext) throws Exception {
        KafkaRecordSerializationSchema.super.open(context, sinkContext);
    }

    /**
     * Serializes a {@link Student} object into a {@link ProducerRecord}.
     *
     * @param event the student event to serialize
     * @param context the Kafka sink context
     * @param timestamp the record timestamp
     * @return the serialized producer record
     */
    @Override
    public ProducerRecord<byte[], byte[]> serialize(Student event, KafkaSinkContext context, Long timestamp) {
        try {
            Headers headers = new RecordHeaders();
            headers.add("published_by", "flink-service".getBytes(StandardCharsets.UTF_8));
            byte[] payload = objectMapper.writeValueAsBytes(event);
            return new ProducerRecord<>(topic, null, (timestamp == null || timestamp < 0L) ? null : timestamp, null, payload, headers);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize Student to JSON", e);
        }
    }
}
