package com.eleks.placescanner.place.service;

import com.eleks.placescanner.place.repository.TestRepository;
import com.eleks.plecescanner.dao.domain.StateTaxDto;
import com.eleks.plecescanner.dao.repository.StateTaxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TestService {
    @Autowired
    TestRepository testRepository;

    @Autowired
    StateTaxRepository stateTaxRepository;

    @Autowired
    MongoTemplate mongoTemplate;

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

        Map<Date, String> dataToMessage = storage.entrySet().stream().collect(Collectors.toMap(e -> new Date(e.getKey()), Map.Entry::getValue));
        return dataToMessage;
    }

    public void clean() {
        testRepository.clean();
    }

    public List<StateTaxDto> findAllStateTaxes() {
        return stateTaxRepository.findAll().stream().map(StateTaxDto::new).toList();
    }
}
