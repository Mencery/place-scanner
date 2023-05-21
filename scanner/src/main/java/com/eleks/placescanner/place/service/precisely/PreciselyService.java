package com.eleks.placescanner.place.service.precisely;

import com.eleks.placescanner.place.domain.DemographicRequest;
import com.eleks.placescanner.place.domain.DemographicResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
