package com.eleks.placescanner.place.service.pollution;

import com.eleks.placescanner.common.domain.pollution.AirQuality;
import com.eleks.placescanner.common.domain.pollution.AirResponse;
import com.eleks.placescanner.common.domain.pollution.PollutionApiResponse;
import com.eleks.placescanner.common.domain.pollution.PollutionResponse;
import com.eleks.placescanner.common.domain.pollution.weather.WeatherResponse;
import com.eleks.placescanner.common.domain.promaptools.Output;
import com.eleks.placescanner.place.service.promaptools.PromaptoolsService;
import com.eleks.placescanner.place.service.utils.TemperatureUtil;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PollutionService {
    @Autowired
    private IqAirClient iqAirClient;

    @Autowired
    private PromaptoolsService promaptoolsService;

    @Value("#{systemEnvironment['IQAIR_API_KEY']}")
    private String key;

    public AirResponse getPollutionByPlace(String zipcode) {
        Output output = promaptoolsService.getLatLongByZip(zipcode);
        String latitude = output.latitude();
        String longitude = output.longitude();

        PollutionApiResponse pollutionApiResponse = iqAirClient.getPollutionByPlace(latitude, longitude, key);
        String airQuality = getAirQualityByIndex(pollutionApiResponse.data().current().pollution().aqius());
        int weatherTemperature = pollutionApiResponse.data().current().weather().temperature();
        int temperature = TemperatureUtil.celsiusToFahrenheit(weatherTemperature);

        PollutionResponse pollutionResponse = new PollutionResponse(airQuality);
        WeatherResponse weatherResponse = new WeatherResponse(temperature);

        return new AirResponse(pollutionResponse, weatherResponse);
    }

    private String getAirQualityByIndex(int airQualityIndex) {
        var airQuality = Arrays
                .stream(AirQuality.values())
                .filter(p -> p.airQualityIndex > airQualityIndex)
                .findFirst();
        return airQuality.get().airQualityName;
    }
}
