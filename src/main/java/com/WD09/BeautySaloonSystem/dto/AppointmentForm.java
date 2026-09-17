package com.WD09.BeautySaloonSystem.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class AppointmentForm {
    private String serviceName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime appointmentDateTime;

    private Double price;
    private String notes;
    private String appointmentType; // STANDARD or VIP
    private String dedicatedStylist;
}