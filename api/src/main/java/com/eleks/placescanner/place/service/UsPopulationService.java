package com.eleks.placescanner.place.service;

import com.eleks.placescanner.common.domain.population.PopClockResponse;
import com.eleks.placescanner.common.exception.domain.UnexpectedResponseException;
import com.eleks.placescanner.dao.entity.UsPopulation;
import com.eleks.placescanner.dao.repository.UsPopulationRepository;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsPopulationService {

    private final UsPopulationRepository usPopulationRepository;

    @Autowired
    public UsPopulationService(UsPopulationRepository usPopulationRepository) {
        this.usPopulationRepository = usPopulationRepository;
    }

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
            throw new UnexpectedResponseException(e.getMessage());
        }
    }

    public CompletableFuture<PopClockResponse> findUsPopulation() {
        return CompletableFuture.supplyAsync(() -> {
            var populations = usPopulationRepository.findAll();
            if (populations.size() == 1) {
                var population = populations.get(0);
                return new PopClockResponse(population.getPopulation(), population.getDate());
            } else {
                throw new UnexpectedResponseException("cannot be more or less than one us population");
            }
        });
    }
}
