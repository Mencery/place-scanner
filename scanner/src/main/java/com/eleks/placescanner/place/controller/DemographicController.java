package com.eleks.placescanner.place.controller;

import com.eleks.placescanner.place.service.DemographicService;
import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.demographic.precisaly.DemographicResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DemographicController {
    @Autowired
    private DemographicService demographicService;

    @PostMapping(value = "demographic/advance", consumes = {"application/json"})
    public ResponseEntity<DemographicResponse> getRecent(
            @RequestBody PlaceRequest request
    ) {
        return new ResponseEntity<>(demographicService.getPopulationForPlace(request.placeName(), request.state()), HttpStatus.OK);
    }
}
