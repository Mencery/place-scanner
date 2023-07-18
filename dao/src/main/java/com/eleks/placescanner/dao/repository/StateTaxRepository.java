package com.eleks.placescanner.dao.repository;

import com.eleks.placescanner.dao.entity.StateTax;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateTaxRepository extends MongoRepository<StateTax, String> {

    Optional<StateTax> findStateTaxByState(String state);
}
