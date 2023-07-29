package com.eleks.placescanner.place.service;

import com.eleks.placescanner.common.domain.demographic.precisaly.DemographicResponse;
import com.eleks.placescanner.place.service.nominatim.NominatimService;
import com.eleks.placescanner.place.service.precisely.PreciselyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemographicService {

    @Autowired
    private NominatimService nominatimService;

    @Autowired
    private PreciselyService preciselyService;

    public DemographicResponse getPopulationForPlace(String placeName, String state) {
        var polygon = nominatimService.getPolygon(placeName, state);
        return preciselyService.getDemographicByPolygon(polygon);
    }
}
