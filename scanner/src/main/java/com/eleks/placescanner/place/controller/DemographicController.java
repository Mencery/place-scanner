package com.eleks.placescanner.place.controller;

import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.demographic.precisaly.DemographicResponse;
import com.eleks.placescanner.place.service.DemographicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemographicController {

    @Autowired
    private DemographicService demographicService;
    @PostMapping(value = "demographic", consumes = {"application/json"})
    @ResponseBody
    public DemographicResponse getRecent(@RequestBody PlaceRequest request) {
        return demographicService.getPopulationForPlace(request.placeName(), request.state());
    }
}
