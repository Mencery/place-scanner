package com.eleks.plecescanner.dao.migration;

import com.eleks.plecescanner.dao.entity.StateTax;
import com.eleks.plecescanner.dao.entity.UsPopulation;
import com.eleks.plecescanner.dao.repository.StateTaxRepository;
import com.eleks.plecescanner.dao.repository.UsPopulationRepository;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ChangeLog
public class DatabaseChangeLog {
    @ChangeSet(order = "001", id = "fillStateTaxes", author = "Denys Plekhanov")
    public void stateTaxDatabase(StateTaxRepository repository) {
        List<StateTax> stateTaxes = new ArrayList<>();
        stateTaxes.add(new StateTax("Alabama", "5", "7.35"));
        stateTaxes.add(new StateTax("Alaska", "0", "1.76"));
        stateTaxes.add(new StateTax("Arizona", "5.1", "8.40"));
        stateTaxes.add(new StateTax("Arkansas", "5.9", "9.55"));
        stateTaxes.add(new StateTax("California", "14.63", "8.82"));
        stateTaxes.add(new StateTax("Colorado", "4.55", "7.77"));
        stateTaxes.add(new StateTax("Connecticut", "4.55", "6.35"));
        stateTaxes.add(new StateTax("Delaware", "6.6", "0"));
        stateTaxes.add(new StateTax("Florida", "0", "7.01"));
        stateTaxes.add(new StateTax("Georgia", "5.75", "7.35"));
        stateTaxes.add(new StateTax("Hawaii", "11", "4.44"));
        stateTaxes.add(new StateTax("Idaho", "6.5", "6.02"));
        stateTaxes.add(new StateTax("Illinois", "4.95", "8.81"));
        stateTaxes.add(new StateTax("Indiana", "3.23", "7"));
        stateTaxes.add(new StateTax("Iowa", "8.53", "6.94"));
        stateTaxes.add(new StateTax("Kansas", "5.7", "8.7"));
        stateTaxes.add(new StateTax("Kentucky", "5", "6"));
        stateTaxes.add(new StateTax("Louisiana", "4.25", "9.55"));
        stateTaxes.add(new StateTax("Maine", "7.15", "5.5"));
        stateTaxes.add(new StateTax("Maryland", "5.75", "6"));
        stateTaxes.add(new StateTax("Massachusetts", "5", "6.25"));
        stateTaxes.add(new StateTax("Michigan", "4.25", "6"));
        stateTaxes.add(new StateTax("Minnesota", "9.85", "7.49"));
        stateTaxes.add(new StateTax("Mississippi", "5", "7.07"));
        stateTaxes.add(new StateTax("Missouri", "5.3", "8.29"));
        stateTaxes.add(new StateTax("Montana", "6.6", "0"));
        stateTaxes.add(new StateTax("Nebraska", "6.95", "6.94"));
        stateTaxes.add(new StateTax("Nevada", "0", "8.23"));
        stateTaxes.add(new StateTax("New Hampshire", "0", "0"));
        stateTaxes.add(new StateTax("New Jersey", "11.8", "6.6"));
        stateTaxes.add(new StateTax("New Mexico", "5.9", "7.84"));
        stateTaxes.add(new StateTax("New York", "11.7", "8.52"));
        stateTaxes.add(new StateTax("North Carolina", "5.09", "6.98"));
        stateTaxes.add(new StateTax("North Dakota", "2.9", "6.96"));
        stateTaxes.add(new StateTax("Ohio", "5.01", "7.22"));
        stateTaxes.add(new StateTax("Oklahoma", "4.75", "8.97"));
        stateTaxes.add(new StateTax("Oregon", "9.9", "0"));
        stateTaxes.add(new StateTax("Pennsylvania", "3.07", "6.34"));
        stateTaxes.add(new StateTax("Rhode Island", "5.99", "7"));
        stateTaxes.add(new StateTax("South Carolina", "7", "7.44"));
        stateTaxes.add(new StateTax("South Dakota", "0", "6.4"));
        stateTaxes.add(new StateTax("Tennessee", "0", "9.55"));
        stateTaxes.add(new StateTax("Texas", "0", "8.2"));
        stateTaxes.add(new StateTax("Utah", "4.95", "7.19"));
        stateTaxes.add(new StateTax("Vermont", "8.75", "6.24"));
        stateTaxes.add(new StateTax("Virginia", "5.75", "5.75"));
        stateTaxes.add(new StateTax("West Virginia", "6.5", "6.52"));
        stateTaxes.add(new StateTax("Wisconsin", "7.65", "5.43"));
        stateTaxes.add(new StateTax("Wyoming", "0", "5.22"));

        repository.insert(stateTaxes);
    }

    @ChangeSet(order = "002", id = "fillUsPopulation", author = "Denys Plekhanov")
    public void usPopulationDatabase(UsPopulationRepository repository) {
        var populations = repository.findAll();
        if (populations.size() == 0) {
            repository.insert(new UsPopulation(334847576, new Date()));
        }
    }
}
