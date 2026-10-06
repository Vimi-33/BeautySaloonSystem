package com.WD09.BeautySaloonSystem.contoller;

import com.WD09.BeautySaloonSystem.entities.Customer;
import com.WD09.BeautySaloonSystem.entities.ServiceEntity;
import com.WD09.BeautySaloonSystem.service.CustomerProfileService;
import com.WD09.BeautySaloonSystem.service.ServiceCatalogService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/customer/services")
public class CustomerServicesController {

    private final ServiceCatalogService serviceCatalogService;
    private final CustomerProfileService customerProfileService;

    public CustomerServicesController(ServiceCatalogService serviceCatalogService,
                                      CustomerProfileService customerProfileService) {
        this.serviceCatalogService = serviceCatalogService;
        this.customerProfileService = customerProfileService;
    }

    @GetMapping
    public String showServicesPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<ServiceEntity> services = serviceCatalogService.getAllServices();

        model.addAttribute("services", services);
        model.addAttribute("activePage", "services");

        if (userDetails != null) {
            Customer customer = customerProfileService.getProfileByEmail(userDetails.getUsername());
            model.addAttribute("customer", customer);
        }

        return "customer/customer-services";
    }
}