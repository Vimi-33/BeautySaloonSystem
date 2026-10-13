package com.WD09.BeautySaloonSystem.service;


import com.WD09.BeautySaloonSystem.dto.FeedbackDto;
import java.util.List;

public interface FeedbackService {
    FeedbackDto createFeedback(FeedbackDto feedbackDto);
    List<FeedbackDto> getAllFeedbacksForAdmin();
    List<FeedbackDto> getFeedbacksByUser(Long userId);
    FeedbackDto updateFeedback(Long id, FeedbackDto feedbackDto, Long userId);
    void deleteFeedback(Long id, Long userId);
}