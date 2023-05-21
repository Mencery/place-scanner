package com.eleks.placescanner.place.service;

import com.eleks.placescanner.place.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TestService {
    @Autowired
    TestRepository testRepository;

    public void save(Long timestamp, String message) {
        testRepository.save(timestamp, message);
    }

    public String findRecent() {
        Map<Long, String> storage = testRepository.findAll();
        Long recent = storage.keySet().stream().max(Comparator.comparing(Long::valueOf)).orElse(0L);
        if (recent == 0) {
            return "nothing found";
        }
        return storage.get(recent);
    }

    public Map<Date, String> findAll() {
        Map<Long, String> storage = testRepository.findAll();
        Date d = new Date();

        Map<Date, String> dataToMessage = storage.entrySet().stream().collect(Collectors.toMap( e -> new Date(e.getKey()),Map.Entry::getValue));
        return dataToMessage;
    }

    public void clean() {
        testRepository.clean();
    }
}
