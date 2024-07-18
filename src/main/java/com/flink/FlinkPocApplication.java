package com.flink;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Main class for the Spring Boot application that integrates with Apache Flink.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.flink")
public class FlinkPocApplication {

    /**
     * The main method to start the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(FlinkPocApplication.class).run(args);
    }

    /**
     * Configures the Jackson {@link ObjectMapper} bean.
     *
     * @return a configured {@link ObjectMapper} instance
     */
    @Bean
    public ObjectMapper oMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(NON_NULL);
        return mapper;
    }
}
