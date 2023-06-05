package com.eleks.placescanner.place.service.pollution;

import com.eleks.plecescanner.common.domain.pollution.PollutionApiResponse;
import com.eleks.plecescanner.common.domain.pollution.PollutionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PollutionService {

    @Value("${iqair.api.country}")
    private String country;

    @Value("${iqair.api.key}")
    private String key;

    @Autowired
    private IqAirClient iqAirClient;

    public PollutionResponse getPollutionByPlace(String city, String state){
        PollutionApiResponse pollutionApiResponse = iqAirClient.getPollutionByPlace(city, state, country, key);
        return new PollutionResponse(pollutionApiResponse.data().current().pollution().aqius());
    }
}
