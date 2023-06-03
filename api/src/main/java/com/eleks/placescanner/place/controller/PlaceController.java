package com.eleks.placescanner.place.controller;

import com.eleks.placescanner.place.service.PlaceService;
import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.PlaceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaceController {

    @Autowired
    PlaceService placeService;

    @PostMapping(value = "place/info", consumes = {"application/json"})
    public ResponseEntity<PlaceResponse> getPlaceInfo(
            @RequestBody PlaceRequest request
    )  {
        return new ResponseEntity<>(placeService.getPlaceInfo(request.validateRequest()), HttpStatus.OK);
    }
}
