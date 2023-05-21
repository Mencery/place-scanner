package com.eleks.placescanner.place.repository;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TestRepository {
    private Map<Long, String> storage = new HashMap<>();

    public void save(Long timestamp, String message) {
        storage.put(timestamp, message);
    }

    public Map<Long, String> findAll() {
        return storage;
    }

    public void clean() {
        storage = new HashMap<>();
    }

}
