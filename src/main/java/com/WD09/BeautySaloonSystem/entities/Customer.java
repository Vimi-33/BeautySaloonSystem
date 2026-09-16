package com.WD09.BeautySaloonSystem.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {

    @Column(name = "loyalty_points")
    private int loyaltyPoints = 0;

    @Column(name = "membership_tier")
    private String membershipTier = "SILVER";

    @Column(name = "total_spent")
    private double totalSpent = 0.0;

    @Override
    public String getRoleName() {
        return "CUSTOMER";
    }

    @Override
    public String getDashboardRedirectUrl() {
        return "redirect:/customer/dashboard";
    }
}