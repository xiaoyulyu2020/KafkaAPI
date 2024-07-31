# Kafka App

![Screenshot 2024-07-27 at 10.20.15.png](Kafka%20App%20e67e5357e8d74c0fa92690f2b870ba7c/Screenshot_2024-07-27_at_10.20.15.png)

Kafka Command

```jsx
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties
bin/kafka-console-consumer.sh --topic your-topic --from-beginning --bootstrap-server localhost:9092

```

Spring Dependence:

1. Producer: kafka, reactive web, lombok, spring-boot-webflux
2. Consumer: kafka, web, lombok

# Producer:

1. Setup the Yaml file
    
    ```jsx
    spring:
      kafka:
        producer:
          bootstrap-servers: localhost:9092
          key-serializer: org.apache.kafka.common.serialization.StringSerializer
          value-serializer: org.apache.kafka.common.serialization.StringSerializer
    #      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    server:
    	port: 8081
    ```
    
    > Since we will have a consumer using 8080 port, we define the producer port as 8081
    > 

1. Topic configuration
    
    ```jsx
    @Configuration
    public class ProducerTopic {
    
        @Bean
        public NewTopic newTopic() {
            return TopicBuilder
                    .name("Wiki")
                    .build();
        }
    }
    ```
    
2. In order to apply react api there is web configuration needs to be set
    
    ```jsx
    package config;
    
    import org.springframework.context.annotation.Bean;
    import org.springframework.web.reactive.function.client.WebClient;
    
    public class WebClientConfig {
    
        @Bean
        public WebClient.Builder webClientBuilder() {
            return WebClient.builder();
        }
    }
    
    ```
    
3. Create Producer function:
    
    ```jsx
    package producer;
    
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
    
    ```
    
4. We have Topic, web api, Producer and configurations ready to use, we need to collect data come from target website.
    
    ```jsx
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
    
    ```
    
5. Notice:
    
    Make sure all the files are under the right directory.
    
6. Consumer in terminal:
    
    ```jsx
    // Create a Consumer running in terminal in order to see the result
    bin/kafka-console-consumer.sh --topic Wiki --from-beginning --bootstrap-server localhost:9092
    ```
    

> Note: Postman [get] [http://localhost:8081/api/v1/wikimedia](http://localhost:8081/api/v1/wikimedia)
> 

# Consumer

1. Configuration:
    
    ```java
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
    // make sure both producer and consumer in the same topic
    ```
    
    ```yaml
    spring:
      kafka:
        consumer:
          bootstrap-servers: localhost:9092
          group-id: myGroup
          auto-offset-reset: earliest
          key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
          value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    
    ```
    
2. Consumer method
    
    ```jsx
    // 1. @KafkaLisener(topics="", groupId="")
    // 2. the method should take "ConsumerRecord<String,Object> name" as parameter.
    // 3. In this position we only need string as input. just use normal parameter as string
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
    
    ```