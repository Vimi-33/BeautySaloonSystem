package com.WD09.BeautySaloonSystem.service;

import com.WD09.BeautySaloonSystem.dto.AppointmentForm;
import com.WD09.BeautySaloonSystem.entities.*;
import com.WD09.BeautySaloonSystem.repository.AppointmentRepository;
import com.WD09.BeautySaloonSystem.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private static final double VIP_SURCHARGE = 1.15;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @Transactional
    public Appointment createAppointment(String customerEmail, AppointmentForm form) {
        Customer customer = customerRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + customerEmail));

        Appointment appointment;

        if ("VIP".equalsIgnoreCase(form.getAppointmentType())) {
            VIPAppointment vip = new VIPAppointment();
            vip.setDedicatedStylist(form.getDedicatedStylist());
            appointment = vip;
        } else {
            appointment = new Appointment();
        }

        appointment.setCustomer(customer);
        appointment.setServiceName(form.getServiceName());
        appointment.setAppointmentDateTime(form.getAppointmentDateTime());

        appointment.setPrice(form.getPrice());
        double finalPrice = appointment.calculateFinalPrice();
        appointment.setPrice(Math.round(finalPrice * 100.0) / 100.0);

        appointment.setNotes(form.getNotes());
        appointment.setStatus(AppointmentStatus.PENDING);

        return appointmentRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsForCustomer(String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + customerEmail));
        return appointmentRepository.findByCustomerOrderByAppointmentDateTimeDesc(customer);
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    @Transactional
    public Appointment updateAppointmentStatus(Long appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id: " + appointmentId));

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }

        // GUARD: if it's already confirmed or accepted, prevent changing it again
        String currentStatus = appointment.getStatus() == null
                ? "" : appointment.getStatus().trim().toUpperCase();
        if (AppointmentStatus.CONFIRMED.equals(currentStatus) || "ACCEPTED".equals(currentStatus)) {
            throw new IllegalStateException("This appointment has already been accepted and cannot be modified.");
        }

        // Normalize the requested status (trim + uppercase)
        String normalizedStatus = status.trim().toUpperCase();

        // ACCEPTED is treated as CONFIRMED
        if ("ACCEPTED".equals(normalizedStatus)) {
            normalizedStatus = AppointmentStatus.CONFIRMED;
        }

        switch (normalizedStatus) {
            case AppointmentStatus.CONFIRMED:
            case AppointmentStatus.CANCELLED:
            case AppointmentStatus.COMPLETED:
            case AppointmentStatus.PENDING:
                appointment.setStatus(normalizedStatus);
                break;
            default:
                throw new IllegalArgumentException("Invalid appointment status: " + status);
        }

        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + id));
    }
}