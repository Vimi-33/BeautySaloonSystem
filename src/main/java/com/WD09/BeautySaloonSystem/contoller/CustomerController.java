package com.WD09.BeautySaloonSystem.contoller;

import com.WD09.BeautySaloonSystem.dto.ChangePasswordForm;
import com.WD09.BeautySaloonSystem.dto.ProfileUpdateForm;
import com.WD09.BeautySaloonSystem.entities.Customer;
import com.WD09.BeautySaloonSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final UserService userService;

    @Autowired
    public CustomerController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String customerDashboard(Authentication auth, Model model) {
        Customer customer = (Customer) userService.findByEmail(auth.getName());
        model.addAttribute("customer", customer);
        return "customer/dashboard";
    }

    @GetMapping("/profile")
    public String viewProfile(Authentication auth, Model model) {
        Customer customer = (Customer) userService.findByEmail(auth.getName());

        model.addAttribute("customer", customer);
        model.addAttribute("activePage", "profile");

        // Only seed fresh forms if a failed submission didn't already put one
        // in the model via redirect flash attributes (see updateProfile below).
        if (!model.containsAttribute("profileForm")) {
            model.addAttribute("profileForm",
                    new ProfileUpdateForm(customer.getFullName(), customer.getEmail(), customer.getPhone()));
        }
        if (!model.containsAttribute("passwordForm")) {
            model.addAttribute("passwordForm", new ChangePasswordForm());
        }

        return "customer/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(Authentication auth,
                                @Valid @ModelAttribute("profileForm") ProfileUpdateForm form,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("profileForm", form);
            redirectAttributes.addFlashAttribute("profileError",
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/customer/profile";
        }

        Customer customer = (Customer) userService.findByEmail(auth.getName());

        try {
            // NOTE: this calls a 4-arg overload (id, fullName, email, phone).
            // Your UserService currently only has the 3-arg (id, fullName, phone)
            // version — add the overload shown in the chat message below.
            userService.updateUserProfile(customer.getId(), form.getFullName(), form.getEmail(), form.getPhone());
            redirectAttributes.addFlashAttribute("profileSuccess", "Your details have been updated.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("profileForm", form);
            redirectAttributes.addFlashAttribute("profileError", ex.getMessage());
        }

        return "redirect:/customer/profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(Authentication auth,
                                 @Valid @ModelAttribute("passwordForm") ChangePasswordForm form,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("passwordError",
                    bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/customer/profile";
        }

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            redirectAttributes.addFlashAttribute("passwordError", "New password and confirmation don't match.");
            return "redirect:/customer/profile";
        }

        Customer customer = (Customer) userService.findByEmail(auth.getName());

        try {
            // NOTE: this method doesn't exist on your UserService yet —
            // add it as shown in the chat message below.
            userService.changePassword(customer.getId(), form.getCurrentPassword(), form.getNewPassword());
            redirectAttributes.addFlashAttribute("passwordSuccess", "Your password has been updated.");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("passwordError", ex.getMessage());
        }

        return "redirect:/customer/profile";
    }
}