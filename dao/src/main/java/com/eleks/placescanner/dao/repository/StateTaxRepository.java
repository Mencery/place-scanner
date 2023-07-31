package com.eleks.placescanner.dao.repository;

import com.eleks.placescanner.dao.entity.StateTax;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateTaxRepository extends MongoRepository<StateTax, String> {

    Optional<StateTax> findStateTaxByState(String state);
}
