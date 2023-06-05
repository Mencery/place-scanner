package com.eleks.placescanner.place.service.pollution;

import com.eleks.placescanner.place.service.promaptools.PromaptoolsService;
import com.eleks.placescanner.place.service.utils.TemperatureUtil;
import com.eleks.plecescanner.common.domain.pollution.AirQuality;
import com.eleks.plecescanner.common.domain.pollution.AirResponse;
import com.eleks.plecescanner.common.domain.pollution.PollutionApiResponse;
import com.eleks.plecescanner.common.domain.pollution.PollutionResponse;
import com.eleks.plecescanner.common.domain.pollution.weather.WeatherResponse;
import com.eleks.plecescanner.common.domain.promaptools.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class PollutionService {

    @Value("#{systemEnvironment['IQAIR_API_KEY']}")
    private String key;

    @Autowired
    private IqAirClient iqAirClient;

    @Autowired
    private PromaptoolsService promaptoolsService;

    public AirResponse getPollutionByPlace(String zipcode){
        Output output = promaptoolsService.getLatLongByZip(zipcode);
        String latitude = output.latitude();
        String longitude = output.longitude();

        PollutionApiResponse pollutionApiResponse = iqAirClient.getPollutionByPlace(latitude, longitude, key);
        String airQuality = getAirQualityByIndex(pollutionApiResponse.data().current().pollution().aqius());
        int temperature = TemperatureUtil.celsiusToFahrenheit(pollutionApiResponse.data().current().weather().temperature());

        PollutionResponse pollutionResponse = new PollutionResponse(airQuality);
        WeatherResponse weatherResponse = new WeatherResponse(temperature);

        return new AirResponse(pollutionResponse, weatherResponse);
    }

    private String getAirQualityByIndex(int airQualityIndex){
        Optional<AirQuality> airQuality = Arrays.stream(AirQuality.values()).filter(p -> p.airQualityIndex > airQualityIndex).findFirst();
        return airQuality.get().airQualityName;
    }
}
