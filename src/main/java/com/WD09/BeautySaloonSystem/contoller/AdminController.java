package com.WD09.BeautySaloonSystem.contoller;

import com.WD09.BeautySaloonSystem.entities.AdminUser;
import com.WD09.BeautySaloonSystem.entities.Appointment;
import com.WD09.BeautySaloonSystem.entities.Customer;
import com.WD09.BeautySaloonSystem.service.AppointmentService;
import com.WD09.BeautySaloonSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AppointmentService appointmentService;

    @Autowired
    public AdminController(UserService userService, AppointmentService appointmentService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    // Single Dynamic View Dashboard Endpoint
    @GetMapping("/dashboard")
    public String adminDashboard(@RequestParam(name = "view", required = false) String view,
                                 Authentication auth,
                                 Model model) {

        AdminUser admin = (AdminUser) userService.findByEmail(auth.getName());
        List<Customer> customers = userService.getAllCustomers();

        String activeView = (view != null && !view.trim().isEmpty()) ? view : "customers";

        model.addAttribute("admin", admin);
        model.addAttribute("customers", customers != null ? customers : Collections.emptyList());
        model.addAttribute("totalCustomers", customers != null ? customers.size() : 0);
        model.addAttribute("currentView", activeView);

        // Fetch appointments when viewing appointments tab
        if ("appointments".equalsIgnoreCase(activeView)) {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            model.addAttribute("appointments", appointments != null ? appointments : Collections.emptyList());
        }

        return "admin/dashboard";
    }

    // Manual Admin Override: Update Customer Membership Tier
    @PostMapping("/customers/update-tier")
    public String updateCustomerTier(@RequestParam Long customerId,
                                     @RequestParam String newTier,
                                     RedirectAttributes redirectAttributes) {
        try {
            userService.updateCustomerTier(customerId, newTier);
            redirectAttributes.addFlashAttribute("successMessage", "Membership tier updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/dashboard?view=customers";
    }

    // Remove / Deactivate Customer Account
    @PostMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Customer account removed successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/dashboard?view=customers";
    }

    // Accept Appointment Endpoint
    @PostMapping("/appointments/accept/{id}")
    public String acceptAppointment(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            // 1. Fetch appointment details
            Appointment appointment = appointmentService.getAppointmentById(id);

            // Prevent double processing if already accepted
            if ("ACCEPTED".equalsIgnoreCase(appointment.getStatus())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Appointment #" + id + " is already accepted.");
                return "redirect:/admin/dashboard?view=appointments";
            }

            // 2. Update status to ACCEPTED
            appointmentService.updateAppointmentStatus(id, "ACCEPTED");

            // 3. Update Customer's Total Spent, Loyalty Points, and Tier
            if (appointment.getCustomer() != null && appointment.getPrice() != null) {
                userService.applyAppointmentRewards(appointment.getCustomer().getId(), appointment.getPrice());
            }

            redirectAttributes.addFlashAttribute("successMessage",
                    "Appointment #" + id + " accepted! Customer metrics and tier updated.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/admin/dashboard?view=appointments";
    }
}