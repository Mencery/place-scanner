package com.eleks.placescanner.place.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class KafkaConsumer {

    @Autowired
    TestService testService;
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    private CountDownLatch latch = new CountDownLatch(1);
    private String payload;

    @KafkaListener(topics = "${test.topic}")
    public void receive(ConsumerRecord<String, String> consumerRecord) {

        LOGGER.info("received payload='{}'", consumerRecord.toString());
        payload = consumerRecord.toString();
        testService.save(consumerRecord.timestamp(), consumerRecord.value());
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

    // other getters
}
