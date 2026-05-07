package com.spamdetector.backend.service;

import com.spamdetector.backend.dto.DashboardStatsResponse;
import com.spamdetector.backend.entity.User;
import com.spamdetector.backend.repository.SpamCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final SpamCheckRepository spamCheckRepository;

    public DashboardStatsResponse getStats(User user) {
        long totalChecks = spamCheckRepository.countByUserId(user.getId());
        long spamDetected = spamCheckRepository.countByUserIdAndPrediction(user.getId(), "SPAM");
        long nonSpamDetected = spamCheckRepository.countByUserIdAndPrediction(user.getId(), "NOT SPAM");

        return DashboardStatsResponse.builder()
                .totalChecks(totalChecks)
                .spamDetected(spamDetected)
                .nonSpamDetected(nonSpamDetected)
                .build();
    }
}
