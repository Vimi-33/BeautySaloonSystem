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

        // Set the status directly as a String
        appointment.setStatus(status.toUpperCase());

        return appointmentRepository.save(appointment);
    }

    @Override
    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with ID: " + id));
    }
}