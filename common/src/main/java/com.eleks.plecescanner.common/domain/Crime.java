package com.eleks.plecescanner.common.domain;

import com.eleks.plecescanner.common.domain.crime.IndexVariable;

import java.util.List;

public record Crime(
        List<IndexVariable> crimes

) {
}
