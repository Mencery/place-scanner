package com.eleks.placescanner.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document("usPopulation")
public class UsPopulation {
    @Id
    private String id;

    private int population;
    private Date date;

    public UsPopulation(int population, Date date) {
        this.population = population;
        this.date = date;
    }
}
