package com.eleks.placescanner.place.service.nominatim;

import com.eleks.placescanner.common.domain.demographic.nominatim.GeoJson;
import com.eleks.placescanner.common.domain.demographic.nominatim.GetPolygonRequest;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import com.eleks.placescanner.place.service.KafkaProducer;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NominatimService {

    private final NominatimClient nominatimClient;

    @Autowired
    NominatimService(NominatimClient nominatimClient) {
        this.nominatimClient = nominatimClient;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    @SuppressWarnings("unchecked")
    public List<List<Double>> getPolygon(String placeName, String state) {
        try {
            var request = new GetPolygonRequest(
                    placeName,
                    state
            );
            var polygon = nominatimClient.callPlacePolygon(request);
            var geoJson = polygon.geojson();
            checkEmpty(geoJson);

            List<List<Double>> geoCoordinates;

            if (Objects.equals(geoJson.type(), "MultiPolygon")) {
                var nestedness3 = (List<Object>) geoJson.coordinates().get(0);
                geoCoordinates = (List<List<Double>>) nestedness3.get(0);
            } else if (Objects.equals(geoJson.type(), "Polygon")) {
                geoCoordinates = (List<List<Double>>) geoJson.coordinates().get(0);
            } else {
                LOGGER.error("cannot parse coordinates");
                throw new UnexpectedResponseException("cannot parse coordinates, unknown type");
            }

            checkType(geoCoordinates);
            return geoCoordinates;
        } catch (ClassCastException e) {
            LOGGER.error("it's not List<List<Double>> type");
            throw new UnexpectedResponseException("it's not List<List<Double>> type");
        }

    }

    private void checkEmpty(GeoJson geoJson) {
        if (geoJson.coordinates().isEmpty()) {
            LOGGER.error("coordinates cannot be empty");
            throw new ResourceNotFoundException("coordinates cannot be empty");
        }
    }

    private void checkType(List<List<Double>> geoCoordinates) {
        try {
            Double coordinateToCheckCast = geoCoordinates.get(0).get(0);
        } catch (ClassCastException e) {
            LOGGER.error("it's not List<List<Double>> type");
            throw new UnexpectedResponseException("it's not List<List<Double>> type");
        }
    }

}
