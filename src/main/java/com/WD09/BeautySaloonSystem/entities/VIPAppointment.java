package com.WD09.BeautySaloonSystem.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("VIP")
@Getter
@Setter
public class VIPAppointment extends Appointment {

    private String dedicatedStylist;
    private static final double VIP_SURCHARGE = 1.15;

    @Override
    public Double calculateFinalPrice() {
        return getPrice() * VIP_SURCHARGE;
    }
}