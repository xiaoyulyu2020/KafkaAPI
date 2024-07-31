package org.kafkaproject.kafkaproducer.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.kafkaproject.kafkaproducer.producer.KafkaStringProducer;

@Service
@Slf4j
public class StreamClient {

    private final WebClient webClientStream;
    private final KafkaStringProducer kafkaProducer;


    public StreamClient(
            WebClient.Builder webClientBuild,
            KafkaStringProducer producer
    ){
        this.webClientStream = webClientBuild
                .baseUrl("https://stream.wikimedia.org/v2")
                .build();
        this.kafkaProducer = producer;
    }

    public void getStreamPublish(){
        webClientStream.get()
                .uri("/stream/recentchange")
                .retrieve()
                .bodyToFlux(String.class)
                .subscribe(kafkaProducer::sendMeg);
    }
}
