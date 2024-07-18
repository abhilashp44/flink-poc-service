package com.flink.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;

import java.io.Serializable;

/**
 * A model class representing a student event.
 * This class encapsulates Kafka headers and the consumer record.
 */
@Data
@AllArgsConstructor
public class StudentEvent implements Serializable {

    private Headers headers;
    private ConsumerRecord<byte[], byte[]> consumerRecord;

}
