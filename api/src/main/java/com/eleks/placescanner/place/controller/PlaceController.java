package com.eleks.placescanner.place.controller;

import com.eleks.placescanner.place.service.PlaceService;
import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.PlaceResponse;
import com.eleks.plecescanner.common.security.GoogleTokenUtil;
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

    @GetMapping(value = {"place-info", "validated/place-info"})
    public ResponseEntity<Object> getPlaceInfo(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            PlaceRequest placeRequest,
            Principal principal
    ) {
        var securityToken = googleTokenUtil.getToken(authorization, principal);
        PlaceRequest validatedRequest;
        try {
            validatedRequest = placeRequest.validateRequest();
        } catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return new ResponseEntity<>(placeService.getPlaceInfo(validatedRequest, securityToken), HttpStatus.OK);
    }

    @PostMapping(value = {"place/info", "validated/place/info"}, consumes = {"application/json"})
    public ResponseEntity<PlaceResponse> postPlaceInfo(
            @RequestBody PlaceRequest request,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            Principal principal
    ) {
        var securityToken = googleTokenUtil.getToken(authorization, principal);
        return new ResponseEntity<>(placeService.getPlaceInfo(request.validateRequest(), securityToken), HttpStatus.OK);
    }
}
