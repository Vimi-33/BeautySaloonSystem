package com.WD09.BeautySaloonSystem.contoller;

import com.WD09.BeautySaloonSystem.entities.ServiceEntity;
import com.WD09.BeautySaloonSystem.service.ServiceCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/services")
public class AdminServiceController {

    private final ServiceCatalogService serviceCatalogService;

    @Autowired
    public AdminServiceController(ServiceCatalogService serviceCatalogService) {
        this.serviceCatalogService = serviceCatalogService;
    }

    @GetMapping
    public String showServicesPage(Model model) {
        model.addAttribute("services", serviceCatalogService.getAllServices());
        return "admin/admin-services";
    }

    @PostMapping("/add")
    public String addService(@ModelAttribute ServiceEntity service, RedirectAttributes redirectAttributes) {
        serviceCatalogService.saveService(service);
        redirectAttributes.addFlashAttribute("successMessage", "Service added successfully!");
        return "redirect:/admin/services";
    }

    @PostMapping("/update/{id}")
    public String updateService(@PathVariable("id") Long id,
                                @ModelAttribute ServiceEntity service,
                                RedirectAttributes redirectAttributes) {
        serviceCatalogService.updateService(id, service);
        redirectAttributes.addFlashAttribute("successMessage", "Service updated successfully!");
        return "redirect:/admin/services";
    }

    @PostMapping("/delete/{id}")
    public String deleteService(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        serviceCatalogService.deleteService(id);
        redirectAttributes.addFlashAttribute("successMessage", "Service deleted successfully!");
        return "redirect:/admin/services";
    }
}