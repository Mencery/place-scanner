package com.eleks.placescanner.place.service.pollution;

import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.pollution.PollutionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pollution")
public class PollutionController {

    @Autowired
    private PollutionService pollutionService;

    @PostMapping
    public ResponseEntity<PollutionResponse> getPollution(@RequestBody PlaceRequest request)  {
        return new ResponseEntity<>(
                pollutionService.getPollutionByPlace(request.placeName(), request.state()),
                HttpStatus.OK
        );
    }

}
