package com.eleks.placescanner.place.controller;

import com.eleks.placescanner.place.service.pollution.PollutionService;
import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.pollution.AirResponse;
import com.eleks.plecescanner.common.exception.domain.ResourceNotFoundException;
import com.eleks.plecescanner.common.exception.domain.UnexpectedResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pollution")
public class PollutionController {

    @Autowired
    private PollutionService pollutionService;

    @PostMapping
    @ResponseBody
    public AirResponse getPollution(@RequestBody PlaceRequest request) {
        return pollutionService.getPollutionByPlace(request.zipCode());
    }
}
