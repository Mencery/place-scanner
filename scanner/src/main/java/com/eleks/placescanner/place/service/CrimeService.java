package com.eleks.placescanner.place.service;

import com.eleks.placescanner.place.service.nominatim.NominatimService;
import com.eleks.placescanner.place.service.precisely.PreciselyService;
import com.eleks.placescanner.place.service.promaptools.PromaptoolsService;
import com.eleks.plecescanner.common.domain.crime.CrimeResponse;
import com.eleks.plecescanner.common.domain.demographic.precisaly.DemographicResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrimeService {
    @Autowired
    private PromaptoolsService promaptoolsService;
    @Autowired
    private PreciselyService preciselyService;

    public CrimeResponse getPopulationForPlace(String zipCode) {
        var longLat = promaptoolsService.getLatLongByZip(zipCode);
        var latitude = Double.parseDouble(longLat.latitude());
        var longitude = Double.parseDouble(longLat.longitude());
        return preciselyService.getCrime(longitude, latitude);
    }
}
