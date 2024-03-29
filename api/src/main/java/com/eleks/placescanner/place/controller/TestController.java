package com.eleks.placescanner.place.controller;

import com.eleks.placescanner.dao.domain.StateTaxDto;
import com.eleks.placescanner.place.service.TestService;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping(value = {"test/recent-message"})
    public String getRecent() {
        return testService.findRecent();
    }


    @GetMapping(value = {"test/all-messages"})
    public Map<Date, String> getAll() {
        return testService.findAll();
    }

    @DeleteMapping(value = {"test/all-messages"})
    public Map<Date, String> clean() {
        testService.clear();
        return testService.findAll();
    }

    @GetMapping(value = {"test/state-taxes"})
    public List<StateTaxDto> getAllStateTaxes() {
        return testService.findAllStateTaxes();
    }

    @GetMapping(value = {"test/user-principal"})
    public Principal getPrincipal(Principal principal) {
        return principal;
    }
}
