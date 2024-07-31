package org.kafkaproject.kafkaproducer.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.kafkaproject.kafkaproducer.stream.StreamClient;

@RestController
@RequestMapping("/api/v1/wikimedia")
@RequiredArgsConstructor
public class WikiController {
    private final StreamClient streamClient;

    @GetMapping
    public void index() {
        streamClient.getStreamPublish();
    }
}
