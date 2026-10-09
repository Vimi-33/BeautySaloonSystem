package com.WD09.BeautySaloonSystem.contoller;

import com.WD09.BeautySaloonSystem.dto.ProductForm;
import com.WD09.BeautySaloonSystem.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    // READ
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/admin-products";
    }

    // CREATE
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute ProductForm form,
                      BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("errorMessage", result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/admin/products";
        }
        try {
            productService.createProduct(form);
            ra.addFlashAttribute("successMessage", "Product added successfully");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/products";
    }

    // UPDATE
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute ProductForm form,
                         BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("errorMessage", result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/admin/products";
        }
        try {
            productService.updateProduct(id, form);
            ra.addFlashAttribute("successMessage", "Product updated successfully");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/products";
    }

    // DELETE
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            productService.deleteProduct(id);
            ra.addFlashAttribute("successMessage", "Product deleted");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/products";
    }
}