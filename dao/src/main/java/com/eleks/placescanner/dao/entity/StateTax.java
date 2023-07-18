package com.eleks.placescanner.dao.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("stateTax")
public class StateTax {

    @Id
    private String id;

    @Indexed(unique = true)
    private String state;
    
    private String individualIncomeTax;
    private String onSaleTax;

    public StateTax(String state, String individualIncomeTax, String onSaleTax) {
        this.state = state;
        this.individualIncomeTax = individualIncomeTax;
        this.onSaleTax = onSaleTax;
    }
}
