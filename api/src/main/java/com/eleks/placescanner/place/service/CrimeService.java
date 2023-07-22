package com.eleks.placescanner.place.service;

import com.eleks.placescanner.place.service.scanner.ScannerClient;
import com.eleks.placescanner.common.domain.Crime;
import com.eleks.placescanner.common.domain.PlaceRequest;
import com.eleks.placescanner.common.exception.domain.ErrorMessage;
import com.eleks.placescanner.common.exception.domain.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class CrimeService {

    private ScannerClient scannerClient;

    @Autowired
    public CrimeService(ScannerClient scannerClient) {
        this.scannerClient = scannerClient;
    }

    public CompletableFuture<Crime> getPlaceCrime(PlaceRequest request, String securityToken, List<ErrorMessage> errorMessages) {
        return CompletableFuture.supplyAsync(() -> {
            var crimeResponse = scannerClient.callCrimeByLocation(request, securityToken, errorMessages);

            if (crimeResponse == null) {
                return null;
            }

            if (crimeResponse.themes().size() == 1) {
                var indexVariables = crimeResponse.themes().get(0).crimeIndexTheme().indexVariable();
                return new Crime(indexVariables);
            } else {
                throw new ResourceNotFoundException("crime shoud have one default theme, theme size: " + crimeResponse.themes().size());
            }
        });
    }
}
