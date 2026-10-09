package com.WD09.BeautySaloonSystem.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("SKIN")
public class SkinProduct extends Product {

    @Override
    public String getCategoryLabel() {
        return "Skin Care";
    }

    @Override
    public String getTypeCode() {
        return "SKIN";
    }

    @Override
    protected BigDecimal getTaxRate() {
        return new BigDecimal("0.12"); // 12% tax rate (adjust as needed)
    }
}