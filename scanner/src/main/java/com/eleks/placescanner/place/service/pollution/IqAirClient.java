package com.eleks.placescanner.place.service.pollution;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import com.eleks.plecescanner.common.domain.pollution.PollutionApiResponse;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "iqAirApi", url = "${iqair.api.url}")
public interface IqAirClient {

    @GetMapping("/nearest_city")
    PollutionApiResponse getPollutionByPlace(
            @RequestParam("lat") String city,
            @RequestParam("lon") String state,
            @RequestParam("key") String key);
}
