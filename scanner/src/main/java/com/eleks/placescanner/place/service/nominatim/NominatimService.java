package com.eleks.placescanner.place.service.nominatim;

import com.eleks.plecescanner.common.domain.demographic.nominatim.GetPolygonRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NominatimService {

    @Autowired
    private NominatimClient nominatimClient;
    @SuppressWarnings("unchecked")
    public List<List<Double>> getPolygon(String placeName, String state) {
        var request = new GetPolygonRequest(
                placeName,
                state
        );
        var polygon = nominatimClient.callPlacePolygon(request);
        var geoJson = polygon.geojson();
        List<List<Double>> geoCoordinates;
        if (Objects.equals(geoJson.type(), "MultiPolygon")) {
            var nestedness3 = (List<Object>) geoJson.coordinates().get(0);
            var nestedness2 = (List<List<Double>>) nestedness3.get(0);
            return nestedness2;
        } else if (Objects.equals(geoJson.type(), "Polygon")) {
            var nestedness2 = (List<List<Double>>) geoJson.coordinates().get(0);
            return nestedness2;
        } else {
            throw new IllegalStateException("cannot parse coordinates");
        }
    }

}
