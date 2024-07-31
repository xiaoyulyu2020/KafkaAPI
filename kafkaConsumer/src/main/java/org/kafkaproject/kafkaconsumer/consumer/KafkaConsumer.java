package org.kafkaproject.kafkaconsumer.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "Wiki", groupId = "myGroup")
    public void consume(String message) {
        log.info(format("The message from KAFKA Producer is: %s",message));
    }
}
