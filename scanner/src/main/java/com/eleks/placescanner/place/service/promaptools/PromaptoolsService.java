package com.eleks.placescanner.place.service.promaptools;

import com.eleks.plecescanner.common.domain.promaptools.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromaptoolsService {

    @Autowired
    private PromaptoolsClient promaptoolsClient;

    public Output getLatLongByZip(String zipCode) {

        var promaptoolsResponse = promaptoolsClient.callLatLngByZip(zipCode);

        if (promaptoolsResponse.outputList().size() > 0) {
            var firstOutput = promaptoolsResponse.outputList().get(0);
            return firstOutput;
        } else {
            throw new IllegalStateException("no latitude or longitude were found by zip code " + zipCode);
        }
    }

}
