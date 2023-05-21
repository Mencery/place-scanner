package com.eleks.placescanner.place.controller;


import com.eleks.placescanner.place.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
public class TestController {
    @Autowired
    TestService testService;

    @GetMapping("test/get/recent")
    public String getRecent()  {
        return testService.findRecent();
    }
    @GetMapping("test/get/all")
    public Map<Date, String> getAll()  {
        return testService.findAll();
    }

    @DeleteMapping("test/storage/clean")
    public Map<Date, String> clean(){
        testService.clean();
        return testService.findAll();
    }


}
