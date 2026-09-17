package com.WD09.BeautySaloonSystem.service;

import com.WD09.BeautySaloonSystem.dto.AppointmentForm;
import com.WD09.BeautySaloonSystem.entities.Appointment;

import java.util.List;

public interface AppointmentService {
    Appointment createAppointment(String customerEmail, AppointmentForm form);
    List<Appointment> getAppointmentsForCustomer(String customerEmail);
    List<Appointment> getAllAppointments();
    Appointment updateAppointmentStatus(Long appointmentId, String status);

    Appointment getAppointmentById(Long id);
}
