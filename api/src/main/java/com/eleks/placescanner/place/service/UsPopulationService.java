package com.eleks.placescanner.place.service;

import com.eleks.plecescanner.common.domain.population.PopClockResponse;
import com.eleks.plecescanner.dao.entity.UsPopulation;
import com.eleks.plecescanner.dao.repository.UsPopulationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UsPopulationService {
    @Autowired
    UsPopulationRepository usPopulationRepository;

    public void updateUsPopulation(PopClockResponse popClock) {
        try {
            var populations = usPopulationRepository.findAll();
            if (populations.size() == 0) {
                usPopulationRepository.insert(
                        new UsPopulation(popClock.population(), popClock.date())
                );
            } else if (populations.size() == 1) {
                var population = populations.get(0);
                population.setPopulation(popClock.population());
                population.setDate(popClock.date());
                usPopulationRepository.save(population);
            } else {
                throw new SQLException("cannot be more or less than one us population");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("cannot be more than one us population");
        }
    }

    public PopClockResponse findUsPopulation() {
        var populations = usPopulationRepository.findAll();
        if (populations.size() == 1) {
            var population = populations.get(0);
            return new PopClockResponse(population.getPopulation(), population.getDate());
        } else {
            throw new IllegalStateException("cannot be more or less than one us population");
        }
    }
}
