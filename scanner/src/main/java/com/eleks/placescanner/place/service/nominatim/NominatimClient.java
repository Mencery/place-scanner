package com.eleks.placescanner.place.service.nominatim;

import com.eleks.placescanner.place.service.KafkaProducer;
import com.eleks.plecescanner.common.domain.demographic.nominatim.GetPolygonRequest;
import com.eleks.plecescanner.common.domain.demographic.nominatim.GetPolygonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

public class NominatimClient {
    private final String placePolygonURI;

    private final RestTemplate restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    public NominatimClient(String placePolygonURI, RestTemplate restTemplate) {
        this.placePolygonURI = placePolygonURI;
        this.restTemplate = restTemplate;
    }

    public GetPolygonResponse callPlacePolygon(GetPolygonRequest request) {
        try {

            var uri = UriComponentsBuilder.fromUriString(
                    placePolygonURI + "?"
                            + "q=" + request.placeName() + ", " + request.state() + ", " + "United States" + "&"
                            + "polygon_geojson=1&format=jsonv2"
            ).build().toUri();
            var requestEntity = buildGetRequest(uri);
            var type = new ParameterizedTypeReference<List<GetPolygonResponse>>() {
            };
            var array = restTemplate.exchange(requestEntity, type).getBody();

            if (array != null && array.size() > 0) {
                return array.get(0);
            } else {
                throw new IllegalStateException("Polygon not found");
            }

        } catch (HttpServerErrorException e) {
            LOGGER.error("Nominatim call exception " + e);
            throw e;
        }
    }

    private RequestEntity<Void> buildGetRequest(URI endpoint) {
        return RequestEntity
                .get(endpoint)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }
}
