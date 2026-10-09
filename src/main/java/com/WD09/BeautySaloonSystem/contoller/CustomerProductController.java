package com.WD09.BeautySaloonSystem.contoller;

import com.WD09.BeautySaloonSystem.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/customer/products")
public class CustomerProductController {

    private final ProductService productService;

    public CustomerProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String viewProducts(Model model) {
        model.addAttribute("products", productService.getAvailableProducts());
        return "customer/customer-products";
    }

    @PostMapping("/purchase/{id}")
    public String purchaseProduct(@PathVariable Long id, RedirectAttributes ra) {
        try {
            productService.purchaseProduct(id, 1); // Deducts 1 item from stock
            ra.addFlashAttribute("successMessage", "Purchase successful!");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/customer/products";
    }
}