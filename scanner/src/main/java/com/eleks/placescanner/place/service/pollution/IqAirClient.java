package com.eleks.placescanner.place.service.pollution;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.eleks.plecescanner.common.domain.pollution.PollutionApiResponse;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "iqAirApi", url = "${iqair.api.url}")
public interface IqAirClient {

    @GetMapping("/city")
    PollutionApiResponse getPollutionByPlace(
            @RequestParam("city") String city,
            @RequestParam("state") String state,
            @RequestParam("country") String country,
            @RequestParam("key") String key);
}
