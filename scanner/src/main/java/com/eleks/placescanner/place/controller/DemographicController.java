package com.eleks.placescanner.place.controller;

import com.eleks.placescanner.place.service.DemographicService;
import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.demographic.precisaly.DemographicResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DemographicController {

    @Autowired
    private DemographicService demographicService;

    @PostMapping(value = "demographic", consumes = {"application/json"})
    public DemographicResponse getRecent(
            @RequestBody PlaceRequest request) {
        return demographicService.getPopulationForPlace(request.placeName(), request.state());
    }
}
