package com.eleks.placescanner.place.controller;

import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.PlaceResponse;
import com.eleks.placescanner.common.security.GoogleTokenUtil;
import com.eleks.placescanner.place.service.PlaceService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlaceController {

    @Autowired
    private GoogleTokenUtil googleTokenUtil;

    @Autowired
    private PlaceService placeService;

    @GetMapping(value = {"place-info"})
    public PlaceResponse getPlaceInfo(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            PlaceRequest placeRequest,
            Principal principal) {
        var securityToken = googleTokenUtil.getToken(authorization, principal);
        return placeService.getPlaceInfo(placeRequest.validateRequest(), securityToken);
    }

    @PostMapping(value = {"places/place-info"}, consumes = {"application/json"})
    public PlaceResponse postPlaceInfo(
            @RequestBody PlaceRequest request,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization,
            Principal principal) {
        var securityToken = googleTokenUtil.getToken(authorization, principal);
        return placeService.getPlaceInfo(request.validateRequest(), securityToken);
    }
}
