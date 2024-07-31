package org.kafkaproject.kafkaproducer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class ProducerTopic {

    @Bean
    public NewTopic newTopic() {
        return TopicBuilder
                .name("Wiki")
                .build();
    }
}
