package com.eleks.placescanner.place.controller;

import com.eleks.placescanner.common.domain.PlaceResponse;
import com.eleks.placescanner.place.service.PlaceService;
import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.security.GoogleTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class PlaceController {

    @Autowired
    GoogleTokenUtil googleTokenUtil;

    @Autowired
    PlaceService placeService;

    @GetMapping(value = {"place-info"})
    public PlaceResponse getPlaceInfo(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            PlaceRequest placeRequest,
            Principal principal) {
        var securityToken = googleTokenUtil.getToken(authorization, principal);
        return placeService.getPlaceInfo(placeRequest.validateRequest(), securityToken);
    }

    @PostMapping(value = {"places/place-info"}, consumes = {"application/json"})
    public ResponseEntity<?> postPlaceInfo(
            @RequestBody PlaceRequest request,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            Principal principal) {
        var securityToken = googleTokenUtil.getToken(authorization, principal);
        return new ResponseEntity<>(placeService.getPlaceInfo(request.validateRequest(), securityToken), HttpStatus.OK);
    }
}
