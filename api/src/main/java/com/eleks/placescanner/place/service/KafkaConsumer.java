package com.eleks.placescanner.place.service;

import com.eleks.plecescanner.common.domain.population.PopClockResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    UsPopulationService usPopulationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    private CountDownLatch latch = new CountDownLatch(1);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${us-population.topic}")
    public void receiveUsPopulation(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {

        LOGGER.info("received payload='{}'", consumerRecord.toString());
        var popClock = objectMapper.readValue(consumerRecord.value(), PopClockResponse.class);

        latch.countDown();

        usPopulationService.updateUsPopulation(popClock);
    }

    @KafkaListener(topics = "${test.topic}")
    public void receive(ConsumerRecord<String, String> consumerRecord) {

        LOGGER.info("received payload='{}'", consumerRecord.toString());
        var payload = consumerRecord.toString();
        testService.save(consumerRecord.timestamp(), consumerRecord.value());
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

}
