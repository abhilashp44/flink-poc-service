package com.flink.config;

import com.flink.deserializers.CustomKafkaRecordDeserializer;
import com.flink.model.StudentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Kafka sources in a Flink application.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class FlinkSourceConfig {

    @Value("${kafka.bootstrap-server}")
    private String bootstrapServer;

    @Value("${kafka.source-topic}")
    private String sourceTopic;

    /**
     * Creates a Kafka source for reading {@link StudentEvent} messages.
     *
     * @return a configured {@link KafkaSource} for {@link StudentEvent} objects.
     */
    @Bean(name = "kafkaSource")
    public KafkaSource<StudentEvent> flinkKafkaSource() {
        return KafkaSource.<StudentEvent>builder()
                .setBootstrapServers(bootstrapServer)
                .setTopics(sourceTopic)
                .setGroupId("flink-group")
                .setDeserializer(new CustomKafkaRecordDeserializer())
                .setStartingOffsets(OffsetsInitializer.latest())
                .build();
    }
}
