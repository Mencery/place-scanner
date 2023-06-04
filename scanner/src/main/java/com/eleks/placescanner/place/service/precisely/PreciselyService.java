package com.eleks.placescanner.place.service.precisely;

import com.eleks.plecescanner.common.domain.crime.CrimeRequest;
import com.eleks.plecescanner.common.domain.crime.CrimeResponse;
import com.eleks.plecescanner.common.domain.demographic.precisaly.DemographicAdvancedRequest;
import com.eleks.plecescanner.common.domain.demographic.precisaly.DemographicRequest;
import com.eleks.plecescanner.common.domain.demographic.precisaly.DemographicResponse;
import com.eleks.plecescanner.common.domain.demographic.precisaly.polygon.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreciselyService {

    @Autowired
    private PreciselyClient preciselyClient;

    public DemographicResponse getDemographic(double longitude, double latitude) {
        var request = new DemographicRequest(
                longitude,
                latitude,
                "Top3Descending",
                "expendituretheme",
                "perCentasAVailable",
                "detailed"
        );
        return preciselyClient.callDemographicByLocation(request);
    }

    public DemographicResponse getDemographicByPolygon(List<List<Double>> coordinates) {
        var request = new DemographicAdvancedRequest(
                new Geometry(coordinates)
        );
        return preciselyClient.callDemographicAdvance(request);
    }

    public CrimeResponse getCrime(double longitude, double latitude) {
        var request = new CrimeRequest(
                longitude,
                latitude,
                "all",
                "N"
        );
        return preciselyClient.callCrimeByLocation(request);
    }

}
