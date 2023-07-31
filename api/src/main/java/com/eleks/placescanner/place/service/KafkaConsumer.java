package com.eleks.placescanner.place.service;

import com.eleks.placescanner.common.domain.population.PopClockResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;



@Component
public class KafkaConsumer {

    @Autowired
    private UsPopulationService usPopulationService;

    private CountDownLatch latch = new CountDownLatch(1);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "${topic.us-population}")
    public void receiveUsPopulation(ConsumerRecord<String, String> consumerRecord) throws JsonProcessingException {

        LOGGER.info("received payload='{}'", consumerRecord.toString());
        var popClock = objectMapper.readValue(consumerRecord.value(), PopClockResponse.class);

        latch.countDown();

        usPopulationService.updateUsPopulation(popClock);
    }
}
