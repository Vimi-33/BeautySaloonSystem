package com.WD09.BeautySaloonSystem.contoller;

import com.WD09.BeautySaloonSystem.dto.FeedbackDto;
import com.WD09.BeautySaloonSystem.repository.UserRepository;
import com.WD09.BeautySaloonSystem.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class FeedbackWebController {

    private final FeedbackService feedbackService;
    private final UserRepository userRepository;

    public FeedbackWebController(FeedbackService feedbackService, UserRepository userRepository) {
        this.feedbackService = feedbackService;
        this.userRepository = userRepository;
    }

    // Gets the ID of the logged-in user (change findByEmail if you log in with username)
    private Long getCurrentUserId(Principal principal) {
        return userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    // Customer: view ONLY their own feedback
    @GetMapping("/customer/feedback")
    public String getCustomerFeedbackPage(Model model, Principal principal) {
        Long userId = getCurrentUserId(principal);
        model.addAttribute("feedbacks", feedbackService.getFeedbacksByUser(userId));
        model.addAttribute("feedbackDto", new FeedbackDto());
        return "customer/customer-feedback";
    }

    // Customer: create feedback
    @PostMapping("/customer/feedback")
    public String createFeedback(@ModelAttribute FeedbackDto feedbackDto, Principal principal) {
        feedbackDto.setUserId(getCurrentUserId(principal));
        feedbackService.createFeedback(feedbackDto);
        return "redirect:/customer/feedback";
    }

    // Customer: update own feedback
    @PostMapping("/customer/feedback/update/{id}")
    public String updateFeedback(@PathVariable Long id,
                                 @ModelAttribute FeedbackDto feedbackDto,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        try {
            feedbackService.updateFeedback(id, feedbackDto, getCurrentUserId(principal));
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/customer/feedback";
    }

    // Customer: delete own feedback
    @PostMapping("/customer/feedback/delete/{id}")
    public String deleteFeedback(@PathVariable Long id,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        try {
            feedbackService.deleteFeedback(id, getCurrentUserId(principal));
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/customer/feedback";
    }

    // Admin: view all feedback across the salon
    @GetMapping("/admin/all-feedbacks")
    public String getAdminFeedbackPage(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacksForAdmin());
        return "admin/admin-feedback";
    }
}