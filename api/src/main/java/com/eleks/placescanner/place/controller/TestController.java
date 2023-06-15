package com.eleks.placescanner.place.controller;


import com.eleks.placescanner.place.service.TestService;
import com.eleks.plecescanner.dao.domain.StateTaxDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {
    @Autowired
    TestService testService;

    @GetMapping(value = {"test/get/recent", "validated/test/get/recent"})
    public String getRecent() {
        return testService.findRecent();
    }


    @GetMapping(value = {"test/get/all", "validated/test/get/all"})
    public Map<Date, String> getAll() {
        return testService.findAll();
    }

    @DeleteMapping(value = {"test/storage/clean", "validated/test/storage/clean"})
    public Map<Date, String> clean() {
        testService.clean();
        return testService.findAll();
    }

    @GetMapping(value = {"test/state/taxes", "validated/test/state/taxes"})
    public List<StateTaxDto> getAllStateTaxes() {
        return testService.findAllStateTaxes();
    }

    @GetMapping(value = {"test/user-principal", "validated/test/user-principal"})
    public Principal getPrincipal(Principal principal) {
        return principal;
    }
}
