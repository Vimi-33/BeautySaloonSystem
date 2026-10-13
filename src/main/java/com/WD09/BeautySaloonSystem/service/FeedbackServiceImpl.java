package com.WD09.BeautySaloonSystem.service;

import com.WD09.BeautySaloonSystem.dto.FeedbackDto;
import com.WD09.BeautySaloonSystem.entities.Feedback;
import com.WD09.BeautySaloonSystem.entities.User;
import com.WD09.BeautySaloonSystem.repository.FeedbackRepository;
import com.WD09.BeautySaloonSystem.repository.UserRepository;
import com.WD09.BeautySaloonSystem.service.FeedbackService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FeedbackDto createFeedback(FeedbackDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));

        Feedback feedback = new Feedback(user, dto.getComment(), dto.getRating());
        Feedback saved = feedbackRepository.save(feedback);
        return mapToDto(saved);
    }

    @Override
    public List<FeedbackDto> getAllFeedbacksForAdmin() {
        return feedbackRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackDto> getFeedbacksByUser(Long userId) {
        return feedbackRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public FeedbackDto updateFeedback(Long id, FeedbackDto dto, Long userId) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));

        if (!feedback.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You can only update your own feedback");
        }

        feedback.setComment(dto.getComment());
        feedback.setRating(dto.getRating());

        Feedback updated = feedbackRepository.save(feedback);
        return mapToDto(updated);
    }

    @Override
    public void deleteFeedback(Long id, Long userId) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));

        if (!feedback.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: You can only delete your own feedback");
        }

        feedbackRepository.delete(feedback);
    }

    private FeedbackDto mapToDto(Feedback feedback) {
        FeedbackDto dto = new FeedbackDto();
        dto.setId(feedback.getId());
        if (feedback.getUser() != null) {
            dto.setUserId(feedback.getUser().getId());
            dto.setUserName(feedback.getUser().getFullName());
        }
        dto.setComment(feedback.getComment());
        dto.setRating(feedback.getRating());
        dto.setCreatedAt(feedback.getCreatedAt()); // Inherited from BaseAuditEntity
        return dto;
    }
}