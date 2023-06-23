package com.eleks.placescanner.place.service.pollution;

import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.pollution.AirResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pollution")
public class PollutionController {

    @Autowired
    private PollutionService pollutionService;

    @PostMapping
    public ResponseEntity<AirResponse> getPollution(@RequestBody PlaceRequest request) {
        return new ResponseEntity<>(
                pollutionService.getPollutionByPlace(request.zipCode()),
                HttpStatus.OK
        );
    }

}
