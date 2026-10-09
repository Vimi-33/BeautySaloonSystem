package com.WD09.BeautySaloonSystem.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("HAIR")
public class HairProduct extends Product {

    @Override
    public String getCategoryLabel() {
        return "Hair Care";
    }

    @Override
    public String getTypeCode() {
        return "HAIR";
    }

    @Override
    protected BigDecimal getTaxRate() {
        return new BigDecimal("0.10"); // 10% tax rate (adjust as needed)
    }
}