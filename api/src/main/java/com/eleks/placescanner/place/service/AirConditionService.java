package com.eleks.placescanner.place.service;

import com.eleks.placescanner.place.service.scanner.ScannerClient;
import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.pollution.AirResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AirConditionService {
    @Autowired
    private ScannerClient scannerClient;

    public AirResponse getAirInfo(PlaceRequest request, String securityToken){
        return scannerClient.getAirInfo(request, securityToken);
    }
}
