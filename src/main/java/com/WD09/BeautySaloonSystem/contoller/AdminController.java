package com.WD09.BeautySaloonSystem.contoller;

import com.WD09.BeautySaloonSystem.entities.AdminUser;
import com.WD09.BeautySaloonSystem.entities.Customer;
import com.WD09.BeautySaloonSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // Admin Dashboard View - Lists all customers and system metrics
    @GetMapping("/dashboard")
    public String adminDashboard(Authentication auth, Model model) {
        AdminUser admin = (AdminUser) userService.findByEmail(auth.getName());
        List<Customer> customers = userService.getAllCustomers();

        model.addAttribute("admin", admin);
        model.addAttribute("customers", customers);
        model.addAttribute("totalCustomers", customers.size());

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
        return "redirect:/admin/dashboard";
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
        return "redirect:/admin/dashboard";
    }
}