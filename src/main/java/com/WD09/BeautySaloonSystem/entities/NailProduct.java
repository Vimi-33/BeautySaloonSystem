package com.WD09.BeautySaloonSystem.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("NAIL")
public class NailProduct extends Product {

    @Override
    public String getCategoryLabel() {
        return "Nail Care";
    }

    @Override
    public String getTypeCode() {
        return "NAIL";
    }

    @Override
    protected BigDecimal getTaxRate() {
        return new BigDecimal("0.08"); // 8% tax rate (adjust as needed)
    }
}