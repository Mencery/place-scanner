package com.eleks.placescanner.dao.repository;

import com.eleks.placescanner.dao.entity.UsPopulation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsPopulationRepository extends MongoRepository<UsPopulation, String> {
}
