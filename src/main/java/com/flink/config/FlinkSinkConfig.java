package com.flink.config;

import com.flink.serializers.CustomKafkaRecordSerializationSchema;
import com.flink.model.Student;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Kafka sinks in a Flink application.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class FlinkSinkConfig {

    @Value("${kafka.even-destination-topic}")
    private String evenDestinationTopic;

    @Value("${kafka.odd-destination-topic}")
    private String oddDestinationTopic;

    @Value("${kafka.bootstrap-server}")
    private String bootstrapServer;

    /**
     * Creates a Kafka sink for messages with an even ID.
     *
     * @return a configured {@link KafkaSink} for {@link Student} objects.
     */
    @Bean
    public KafkaSink<Student> evenKafkaSink() {
        return KafkaSink.<Student>builder()
                .setBootstrapServers(bootstrapServer)
                .setRecordSerializer(new CustomKafkaRecordSerializationSchema(evenDestinationTopic))
                .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();
    }

    /**
     * Creates a Kafka sink for messages with an odd ID.
     *
     * @return a configured {@link KafkaSink} for {@link Student} objects.
     */
    @Bean
    public KafkaSink<Student> oddKafkaSink() {
        return KafkaSink.<Student>builder()
                .setBootstrapServers(bootstrapServer)
                .setRecordSerializer(new CustomKafkaRecordSerializationSchema(oddDestinationTopic))
                .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();
    }
}
