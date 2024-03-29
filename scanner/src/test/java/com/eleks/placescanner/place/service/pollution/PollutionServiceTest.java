package com.eleks.placescanner.place.service.pollution;

import static com.eleks.placescanner.place.Util.JSON_OBJECTS_FOLDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.eleks.placescanner.common.domain.pollution.AirResponse;
import com.eleks.placescanner.common.domain.pollution.PollutionApiResponse;
import com.eleks.placescanner.common.domain.pollution.PollutionResponse;
import com.eleks.placescanner.common.domain.pollution.weather.WeatherResponse;
import com.eleks.placescanner.common.domain.promaptools.Output;
import com.eleks.placescanner.place.service.promaptools.PromaptoolsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class PollutionServiceTest {

    private static final String IQ_AIR_RESPONSE_PATH = JSON_OBJECTS_FOLDER + "airIQResponse.json";

    @Autowired
    private PollutionService pollutionService;

    @MockBean
    private PromaptoolsService promaptoolsService;

    @MockBean
    private IqAirClient iqAirClient;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();

        when(promaptoolsService.getLatLongByZip(any())).thenReturn(new Output("60090", "42.13919", "-87.92896"));
    }

    @Test
    public void shouldReturnPollutionResponse_whenCallGetPollution() throws Exception {
        var file = new File(IQ_AIR_RESPONSE_PATH);
        PollutionApiResponse iqAirResponse = objectMapper.readValue(file, PollutionApiResponse.class);

        when(iqAirClient.getPollutionByPlace(any(), any(), any())).thenReturn(iqAirResponse);

        var expectedResponse = new AirResponse(new PollutionResponse("Good"), new WeatherResponse(82));
        var actualResponse = pollutionService.getPollutionByPlace("zipcode");

        assertEquals(expectedResponse, actualResponse);
    }
}