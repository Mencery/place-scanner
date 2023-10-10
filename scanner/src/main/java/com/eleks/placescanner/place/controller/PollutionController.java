package com.eleks.placescanner.place.controller;

import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.pollution.AirResponse;
import com.eleks.placescanner.place.service.pollution.PollutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
