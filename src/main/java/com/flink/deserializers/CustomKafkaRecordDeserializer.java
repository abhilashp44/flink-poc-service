package com.flink.deserializers;

import com.flink.model.StudentEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Custom deserializer for Kafka records that converts Kafka messages into {@link StudentEvent} objects.
 */
@Slf4j
public class CustomKafkaRecordDeserializer implements KafkaRecordDeserializationSchema<StudentEvent> {

    /**
     * Deserializes the given Kafka {@link ConsumerRecord} into a {@link StudentEvent} and collects it.
     *
     * @param consumerRecord the Kafka record to deserialize
     * @param collector the collector to which the deserialized {@link StudentEvent} is emitted
     */
    @Override
    public void deserialize(ConsumerRecord<byte[], byte[]> consumerRecord, Collector<StudentEvent> collector) {
        try {
            collector.collect(new StudentEvent(consumerRecord.headers(), consumerRecord));
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize JSON to StudentEvent", e);
        }
    }

    /**
     * Returns the {@link TypeInformation} for the produced {@link StudentEvent} type.
     *
     * @return the type information for {@link StudentEvent}
     */
    @Override
    public TypeInformation<StudentEvent> getProducedType() {
        return TypeInformation.of(StudentEvent.class);
    }
}
