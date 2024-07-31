package org.kafkaproject.kafkaproducer.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaStringProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMeg(String message) {
        log.info("send message: {}", message);
        kafkaTemplate.send("Wiki", message);
    }
}
