package com.WD09.BeautySaloonSystem.contoller;


import com.WD09.BeautySaloonSystem.entities.Customer;
import com.WD09.BeautySaloonSystem.entities.StaffEntity;
import com.WD09.BeautySaloonSystem.service.StaffManagementService;
import com.WD09.BeautySaloonSystem.service.CustomerProfileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/customer/staff")
public class CustomerStaffController {

    private final StaffManagementService staffManagementService;
    private final CustomerProfileService customerProfileService;

    public CustomerStaffController(StaffManagementService staffManagementService,
                                   CustomerProfileService customerProfileService) {
        this.staffManagementService = staffManagementService;
        this.customerProfileService = customerProfileService;
    }

    @GetMapping
    public String showStaffPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<StaffEntity> staffMembers = staffManagementService.getAllStaff();

        model.addAttribute("staffMembers", staffMembers);
        model.addAttribute("activePage", "staff");

        if (userDetails != null) {
            Customer customer = customerProfileService.getProfileByEmail(userDetails.getUsername());
            model.addAttribute("customer", customer);
        }

        return "customer/customer-staff";
    }
}