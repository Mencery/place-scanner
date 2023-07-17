package com.eleks.placescanner.place.service;

import com.eleks.placescanner.place.service.scanner.ScannerClient;
import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.pollution.AirResponse;
import com.eleks.plecescanner.common.exception.domain.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirConditionService {

    @Autowired
    private ScannerClient scannerClient;

    public AirResponse getAirInfo(PlaceRequest request, String securityToken, List<ErrorMessage> errorMessages){
        return scannerClient.getAirInfo(request, securityToken, errorMessages);
    }
}
