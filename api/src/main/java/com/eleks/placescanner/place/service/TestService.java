package com.eleks.placescanner.place.service;

import com.eleks.placescanner.dao.domain.StateTaxDto;
import com.eleks.placescanner.dao.repository.StateTaxRepository;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestService {

    @Autowired
    private StateTaxRepository stateTaxRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<Long, String> kafkaTestMap = new HashMap<>();

    public void save(Long timestamp, String message) {
        kafkaTestMap.put(timestamp, message);
    }

    public String findRecent() {

        Long recent = kafkaTestMap.keySet().stream().max(Comparator.comparing(Long::valueOf)).orElse(0L);
        if (recent == 0) {
            return "nothing found";
        }
        return kafkaTestMap.get(recent);
    }

    public Map<Date, String> findAll() {
        Map<Date, String> dataToMessage = kafkaTestMap.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> new Date(e.getKey()), Map.Entry::getValue));
        return dataToMessage;
    }

    public void clear() {
        kafkaTestMap.clear();
    }

    public List<StateTaxDto> findAllStateTaxes() {
        return stateTaxRepository.findAll().stream().map(StateTaxDto::new).toList();
    }
}
