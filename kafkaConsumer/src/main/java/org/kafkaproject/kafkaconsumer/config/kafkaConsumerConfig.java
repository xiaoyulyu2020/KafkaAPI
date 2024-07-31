package org.kafkaproject.kafkaconsumer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class kafkaConsumerConfig {
    @Bean
    public NewTopic newTopic() {
        return TopicBuilder
                .name("Wiki")
                .build();
    } // same method with producer's

}
