package com.eleks.placescanner.place.controller;


import com.eleks.placescanner.place.service.TestService;
import com.eleks.plecescanner.dao.domain.StateTaxDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {
    @Autowired
    TestService testService;

    @GetMapping("test/get/recent")
    public String getRecent() {
        return testService.findRecent();
    }

    @GetMapping("test/get/all")
    public Map<Date, String> getAll() {
        return testService.findAll();
    }

    @DeleteMapping("test/storage/clean")
    public Map<Date, String> clean() {
        testService.clean();
        return testService.findAll();
    }

    @GetMapping("test/state/taxes")
    public List<StateTaxDto> getAllStateTaxes() {
        return testService.findAllStateTaxes();
    }

}
