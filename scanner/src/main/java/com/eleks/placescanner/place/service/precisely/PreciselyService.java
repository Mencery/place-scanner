package com.eleks.placescanner.place.service.precisely;

import com.eleks.placescanner.common.domain.crime.CrimeRequest;
import com.eleks.placescanner.common.domain.crime.CrimeResponse;
import com.eleks.placescanner.common.domain.demographic.precisaly.DemographicAdvancedRequest;
import com.eleks.placescanner.common.domain.demographic.precisaly.DemographicRequest;
import com.eleks.placescanner.common.domain.demographic.precisaly.DemographicResponse;
import com.eleks.placescanner.common.domain.demographic.precisaly.polygon.Geometry;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PreciselyService {

    private final PreciselyClient preciselyClient;

    @Autowired
    PreciselyService(PreciselyClient preciselyClient) {
        this.preciselyClient = preciselyClient;
    }

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
