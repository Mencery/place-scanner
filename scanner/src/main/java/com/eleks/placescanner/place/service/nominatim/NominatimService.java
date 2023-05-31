package com.eleks.placescanner.place.service.nominatim;

import com.eleks.plecescanner.common.domain.demographic.nominatim.GetPolygonRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class NominatimService {

    @Autowired
    private NominatimClient nominatimClient;

    public List<List<Double>> getPolygon(String placeName, String state) {
        var request = new GetPolygonRequest(
                placeName,
                state
        );
        var polygon = nominatimClient.callPlacePolygon(request);
        var geoJson = polygon.geojson();
        var coordinates = geoJson.coordinates()
                .stream()
                .map(x -> ((List<Object>)x)
                        .stream()
                        .map(y -> (List<Double>)y )
                        .toList())
                .findFirst();

        if(coordinates.isPresent()){
            return coordinates.get();
        }else {
            throw new IllegalStateException("cannot parse coordinates");
        }
    }

}
