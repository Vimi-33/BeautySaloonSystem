package com.WD09.BeautySaloonSystem.contoller;

import com.WD09.BeautySaloonSystem.entities.StaffEntity;
import com.WD09.BeautySaloonSystem.repository.StaffRepository;
import com.WD09.BeautySaloonSystem.service.StaffManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/admin/staff")
public class AdminStaffController {

    private static final Path UPLOAD_DIR = Paths.get("uploads", "staff");

    private final StaffManagementService staffManagementService;
    private final StaffRepository staffRepository;

    @Autowired
    public AdminStaffController(StaffManagementService staffManagementService,
                                StaffRepository staffRepository) {
        this.staffManagementService = staffManagementService;
        this.staffRepository = staffRepository;
    }

    @GetMapping
    public String showStaffPage(Model model) {
        model.addAttribute("staffMembers", staffManagementService.getAllStaff());
        return "admin/admin-staff";
    }

    @PostMapping("/add")
    public String addStaff(@ModelAttribute StaffEntity staff,
                           @RequestParam(value = "photo", required = false) MultipartFile photo,
                           RedirectAttributes redirectAttributes) {
        try {
            if (photo != null && !photo.isEmpty()) {
                staff.setPhotoUrl(storePhoto(photo));
            }
        } catch (IllegalArgumentException | IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/staff";
        }
        staffManagementService.saveStaff(staff);
        redirectAttributes.addFlashAttribute("successMessage", "Staff member added successfully!");
        return "redirect:/admin/staff";
    }

    @PostMapping("/update/{id}")
    public String updateStaff(@PathVariable("id") Long id,
                              @ModelAttribute StaffEntity staff,
                              @RequestParam(value = "photo", required = false) MultipartFile photo,
                              RedirectAttributes redirectAttributes) {
        String newPhotoUrl = null;
        try {
            if (photo != null && !photo.isEmpty()) {
                newPhotoUrl = storePhoto(photo);
            }
        } catch (IllegalArgumentException | IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/staff";
        }

        staffManagementService.updateStaff(id, staff);

        // Only touch the photo when a new one was chosen; otherwise the old photo stays
        if (newPhotoUrl != null) {
            final String url = newPhotoUrl;
            staffRepository.findById(id).ifPresent(existing -> {
                existing.setPhotoUrl(url);
                staffRepository.save(existing);
            });
        }
        redirectAttributes.addFlashAttribute("successMessage", "Staff member updated successfully!");
        return "redirect:/admin/staff";
    }

    @PostMapping("/delete/{id}")
    public String deleteStaff(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        staffManagementService.deleteStaff(id);
        redirectAttributes.addFlashAttribute("successMessage", "Staff member deleted successfully!");
        return "redirect:/admin/staff";
    }

    /** Validates the image, saves it with a random name and returns the URL to store in the database. */
    private String storePhoto(MultipartFile photo) throws IOException {
        String ext;
        switch (String.valueOf(photo.getContentType())) {
            case "image/jpeg": ext = ".jpg"; break;
            case "image/png":  ext = ".png"; break;
            case "image/webp": ext = ".webp"; break;
            default: throw new IllegalArgumentException("Please upload a JPG, PNG or WebP image.");
        }
        Files.createDirectories(UPLOAD_DIR);
        String filename = UUID.randomUUID() + ext;
        Files.copy(photo.getInputStream(), UPLOAD_DIR.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/staff/" + filename;
    }
}