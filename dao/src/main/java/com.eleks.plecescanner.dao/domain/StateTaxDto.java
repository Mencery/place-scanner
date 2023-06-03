package com.eleks.plecescanner.dao.domain;

import com.eleks.plecescanner.dao.entity.StateTax;
import lombok.Data;

@Data
public class StateTaxDto {
    private String state;
    private String individualIncomeTax;
    private String onSaleTax;

    public StateTaxDto(String state, String individualIncomeTax, String onSaleTax) {
        this.state = state;
        this.individualIncomeTax = individualIncomeTax;
        this.onSaleTax = onSaleTax;
    }

    public StateTaxDto(StateTax stateTax) {
        this(
                stateTax.getState(),
                stateTax.getIndividualIncomeTax(),
                stateTax.getOnSaleTax()
        );
    }
}
