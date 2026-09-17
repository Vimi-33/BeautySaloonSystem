package com.WD09.BeautySaloonSystem.contoller;

import com.WD09.BeautySaloonSystem.dto.AppointmentForm;
import com.WD09.BeautySaloonSystem.entities.Appointment;
import com.WD09.BeautySaloonSystem.service.AppointmentService;
import com.WD09.BeautySaloonSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, UserService userService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
    }

    // Customer Views Personal Bookings: GET /customer/appointments
    @GetMapping("/customer/appointments")
    public String viewMyAppointments(Authentication authentication, Model model) {
        String userEmail = authentication.getName();
        model.addAttribute("appointments", appointmentService.getAppointmentsForCustomer(userEmail));
        return "customer/my-appointments";
    }

    // Customer Booking Form: GET /customer/appointments/book
    @GetMapping("/customer/appointments/book")
    public String showBookingForm(Model model) {
        model.addAttribute("appointmentForm", new AppointmentForm());
        return "customer/book-appointment";
    }

    // Customer Submit Booking: POST /customer/appointments/book
    @PostMapping("/customer/appointments/book")
    public String processBooking(@ModelAttribute("appointmentForm") AppointmentForm form,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        try {
            appointmentService.createAppointment(authentication.getName(), form);
            return "redirect:/customer/appointments?success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/customer/appointments/book";
        }
    }

    // Admin Views All Bookings: GET /admin/appointments
    @GetMapping("/admin/appointments")
    @PreAuthorize("hasRole('ADMIN')")
    public String viewAllAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        return "admin/all-appointments";
    }

    // Admin Updates Status: POST /admin/appointments/update-status
    @PostMapping("/admin/appointments/update-status")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateStatus(@RequestParam("id") Long id,
                               @RequestParam("status") String status,
                               RedirectAttributes redirectAttributes) {
        try {
            Appointment appointment = appointmentService.getAppointmentById(id);

            // Prevent double reward processing if appointment is already ACCEPTED
            if ("ACCEPTED".equalsIgnoreCase(appointment.getStatus()) && "ACCEPTED".equalsIgnoreCase(status)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Appointment #" + id + " is already accepted.");
                return "redirect:/admin/appointments";
            }

            // 1. Update appointment status in database
            appointmentService.updateAppointmentStatus(id, status);

            // 2. Automatically trigger customer rewards (total spent, loyalty points, tier) if status is ACCEPTED
            if ("ACCEPTED".equalsIgnoreCase(status) && appointment.getCustomer() != null) {
                userService.applyAppointmentRewards(appointment.getCustomer().getId(), appointment.getPrice());
                redirectAttributes.addFlashAttribute("successMessage",
                        "Appointment #" + id + " accepted! Customer metrics and tier updated.");
            } else {
                redirectAttributes.addFlashAttribute("successMessage",
                        "Appointment #" + id + " status updated to " + status);
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/appointments";
    }
}