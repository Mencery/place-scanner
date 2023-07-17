package com.eleks.placescanner.place.service;

import com.eleks.placescanner.place.service.scanner.ScannerClient;
import com.eleks.plecescanner.common.domain.Crime;
import com.eleks.plecescanner.common.domain.PlaceRequest;
import com.eleks.plecescanner.common.exception.domain.ErrorMessage;
import com.eleks.plecescanner.common.exception.domain.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CrimeService {

    @Autowired
    private ScannerClient scannerClient;

    public Crime getPlaceCrime(PlaceRequest request, String securityToken, List<ErrorMessage> errorMessages) {
        var crimeResponse = scannerClient.callCrimeByLocation(request, securityToken, errorMessages);

        if(crimeResponse == null){
            return null;
        }

        if (crimeResponse.themes().size() == 1) {
            var indexVariables = crimeResponse.themes().get(0).crimeIndexTheme().indexVariable();
            return new Crime(indexVariables);
        } else {
            throw new ResourceNotFoundException("crime shoud have one default theme, theme size: " + crimeResponse.themes().size());
        }
    }
}
