package com.eleks.placescanner.place.service;

import com.eleks.placescanner.place.service.scanner.ScannerClient;
import com.eleks.plecescanner.common.domain.Crime;
import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.domain.crime.CrimeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrimeService {
    @Autowired
    private ScannerClient scannerClient;

    public Crime getPlaceCrime(PlaceRequest request) {
        var crimeResponse = scannerClient.callCrimeByLocation(request);
        if (crimeResponse.themes().size() == 1) {
            var indexVariables = crimeResponse.themes().get(0).crimeIndexTheme().indexVariable();
            return new Crime(indexVariables);
        } else {
            throw new IllegalStateException("crime shoud have one default theme, theme size: " + crimeResponse.themes().size());
        }
    }

}
