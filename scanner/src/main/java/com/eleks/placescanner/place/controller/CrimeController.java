package com.eleks.placescanner.place.controller;

import com.eleks.placescanner.place.service.CrimeService;
import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.crime.CrimeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrimeController {
    @Autowired
    private CrimeService crimeService;

    @PostMapping(value = "crime-by-location", consumes = {"application/json"})
    public ResponseEntity<CrimeResponse> getRecent(
            @RequestBody PlaceRequest request) {
        return new ResponseEntity<>(crimeService.getPopulationForPlace(request.zipCode()), HttpStatus.OK);
    }
}
