package com.WD09.BeautySaloonSystem.controller;

import com.WD09.BeautySaloonSystem.dto.FeedbackDto;
import com.WD09.BeautySaloonSystem.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FeedbackWebController {

    private final FeedbackService feedbackService;

    public FeedbackWebController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // Customer: View their feedback list
    @GetMapping("/customer/feedback")
    public String getCustomerFeedbackPage(Model model) {
        Long currentUserId = 1L; // Replace with active session/security user ID
        model.addAttribute("feedbacks", feedbackService.getFeedbacksByUser(currentUserId));
        return "customer/customer-feedback"; // Points to templates/customer/customer-feedback.html
    }

    // Customer: Create feedback
    @PostMapping("/customer/feedback")
    public String createFeedback(@ModelAttribute FeedbackDto feedbackDto) {
        Long currentUserId = 1L;
        feedbackDto.setUserId(currentUserId);
        feedbackService.createFeedback(feedbackDto);
        return "redirect:/customer/feedback";
    }

    // Customer: Delete feedback
    @PostMapping("/customer/feedback/delete/{id}")
    public String deleteFeedback(@PathVariable Long id) {
        Long currentUserId = 1L;
        feedbackService.deleteFeedback(id, currentUserId);
        return "redirect:/customer/feedback";
    }

    // Admin: View all feedback across the salon
    @GetMapping("/admin/all-feedbacks")
    public String getAdminFeedbackPage(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacksForAdmin());
        return "admin/admin-feedback"; // Points to templates/admin/admin-feedback.html
    }
}