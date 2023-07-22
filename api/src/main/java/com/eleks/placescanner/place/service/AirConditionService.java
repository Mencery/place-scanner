package com.eleks.placescanner.place.service;

import com.eleks.placescanner.place.service.scanner.ScannerClient;
import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.domain.pollution.AirResponse;
import com.eleks.placescanner.common.exception.domain.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AirConditionService {

    private ScannerClient scannerClient;

    @Autowired
    public AirConditionService(ScannerClient scannerClient) {
        this.scannerClient = scannerClient;
    }

    public CompletableFuture<AirResponse> getAirInfo(PlaceRequest request, String securityToken, List<ErrorMessage> errorMessages) {
        return CompletableFuture.supplyAsync(() -> scannerClient.getAirInfo(request, securityToken, errorMessages));

    }

}
