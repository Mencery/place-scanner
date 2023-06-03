package com.eleks.placescanner.place.service;

import com.eleks.placescanner.place.service.census.CensusService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    @Value("${us-population.topic}")
    private String usPopulationTopic;

    @Autowired
    private CensusService censusService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String payload) {
        LOGGER.info("sending payload='{}' to topic='{}'", payload, topic);
        kafkaTemplate.send(topic, payload);
    }

    @Scheduled(cron = "0 */2 * * * *")
    public void sendUsPopulationInformation() throws JsonProcessingException {
        var payload = censusService.getPopulationByClock();
        var jsonPayload = objectMapper.writeValueAsString(payload);
        LOGGER.info("sending payload='{}' to topic='{}'", jsonPayload, usPopulationTopic);
        kafkaTemplate.send(usPopulationTopic, jsonPayload);
    }
}
