package com.eleks.placescanner.place.service.promaptools;

import com.eleks.plecescanner.common.domain.promaptools.Output;
import com.eleks.plecescanner.common.exception.domain.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromaptoolsService {

    private final PromaptoolsClient promaptoolsClient;

    @Autowired
    PromaptoolsService(PromaptoolsClient promaptoolsClient){
        this.promaptoolsClient = promaptoolsClient;
    }

    public Output getLatLongByZip(String zipCode) {

        var promaptoolsResponse = promaptoolsClient.callLatLngByZip(zipCode);

        if (promaptoolsResponse.outputList().size() > 0) {
            return promaptoolsResponse.outputList().get(0);
        } else {
            throw new ResourceNotFoundException("no latitude or longitude were found by zip code " + zipCode);
        }
    }
}
