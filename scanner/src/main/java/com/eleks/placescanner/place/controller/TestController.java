package com.eleks.placescanner.place.controller;


import com.eleks.placescanner.place.service.DemographicService;
import com.eleks.placescanner.place.service.KafkaProducer;
import com.eleks.placescanner.place.service.census.CensusService;
import com.eleks.placescanner.place.service.nominatim.NominatimService;
import com.eleks.placescanner.place.service.precisely.PreciselyService;
import com.eleks.plecescanner.common.domain.demographic.precisaly.DemographicResponse;
import com.eleks.plecescanner.common.domain.population.PopClockResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private KafkaProducer producer;

    @Autowired
    private PreciselyService preciselyService;

    @Autowired
    private DemographicService demographicService;

    @Autowired
    private CensusService censusService;

    @Autowired
    private NominatimService nominatimService;

    @Value("${topic.test}")
    private String topic;

    @GetMapping("test/kafka/send")
    public String kafkaTest(@RequestParam(value = "text", defaultValue = "No text") String text) {
        producer.send(topic, text);
        return "sent";
    }

    @GetMapping("test/demographic")
    public DemographicResponse preciselyDemographicTest(@RequestParam(value = "longitude") Double longitude,
                                                        @RequestParam(value = "latitude") Double latitude) {
        return preciselyService.getDemographic(longitude, latitude);
    }

    @GetMapping("test/demographic-advance")
    public DemographicResponse preciselyDemographicAdvanceTest(@RequestParam(value = "placeName") String placeName,
                                                               @RequestParam(value = "state") String state) {
        return demographicService.getPopulationForPlace(placeName, state);
    }

    @GetMapping("test/popclock-data")
    public PopClockResponse getUSPopulationByClock() {
        return censusService.getPopulationByClock();
    }

    @GetMapping("test/nominant-coordinates")
    public List<List<Double>> getCoordinates(@RequestParam(value = "placeName") String placeName,
                                             @RequestParam(value = "state") String state) {
        return nominatimService.getPolygon(placeName, state);
    }
}
