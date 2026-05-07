package com.spamdetector.backend.service;

import com.spamdetector.backend.dto.SpamCheckRequest;
import com.spamdetector.backend.dto.SpamCheckResponse;
import com.spamdetector.backend.entity.SpamCheck;
import com.spamdetector.backend.entity.User;
import com.spamdetector.backend.repository.SpamCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpamService {

    private final SpamCheckRepository spamCheckRepository;

    // Compiled regex patterns for efficiency
    private static final Pattern URL_PATTERN = Pattern.compile("https?://\\S+|www\\.\\S+");
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("[^a-zA-Z0-9\\s]");
    
    private static final List<String> SPAM_KEYWORDS = List.of(
            "free", "win", "lottery", "urgent", "click now", "click below",
            "congratulations", "claim reward", "earn money", "guaranteed", 
            "cash prize", "act now", "limited time", "special offer",
            "exclusive", "reward", "investment", "earn", "discount"
    );

    @Transactional
    public SpamCheckResponse checkMessage(SpamCheckRequest request, User user) {
        String message = request.getMessage();
        double score = calculateSpamScore(message);
        
        String prediction = score >= 60.0 ? "SPAM" : "NOT SPAM";
        String reason = generateReason(message, score);

        SpamCheck spamCheck = SpamCheck.builder()
                .user(user)
                .message(message)
                .prediction(prediction)
                .confidence(score)
                .reason(reason)
                .build();

        spamCheck = spamCheckRepository.save(spamCheck);

        return SpamCheckResponse.builder()
                .id(spamCheck.getId())
                .message(spamCheck.getMessage())
                .prediction(spamCheck.getPrediction())
                .confidence(spamCheck.getConfidence())
                .reason(spamCheck.getReason())
                .checkedAt(spamCheck.getCheckedAt())
                .build();
    }

    public List<SpamCheckResponse> getHistory(User user) {
        List<SpamCheck> checks = spamCheckRepository.findByUserIdOrderByCheckedAtDesc(user.getId());
        return checks.stream().map(check -> SpamCheckResponse.builder()
                .id(check.getId())
                .message(check.getMessage())
                .prediction(check.getPrediction())
                .confidence(check.getConfidence())
                .reason(check.getReason())
                .checkedAt(check.getCheckedAt())
                .build()
        ).collect(Collectors.toList());
    }

    private double calculateSpamScore(String message) {
        if (message == null || message.trim().isEmpty()) {
            return 0.0;
        }

        double score = 0.0;
        String lowerMessage = message.toLowerCase();

        // 1. Keyword check
        int keywordCount = 0;
        for (String keyword : SPAM_KEYWORDS) {
            if (lowerMessage.contains(keyword)) {
                keywordCount++;
                score += 30.0; // Each keyword adds 30%
            }
        }

        // 2. URL check
        if (URL_PATTERN.matcher(message).find()) {
            score += 35.0;
        }

        // 3. Uppercase check
        long upperCount = UPPERCASE_PATTERN.matcher(message).results().count();
        double upperRatio = (double) upperCount / message.length();
        if (upperRatio > 0.3) { // If more than 30% are uppercase
            score += 45.0;
        }

        // 4. Special char check
        long specialCount = SPECIAL_CHAR_PATTERN.matcher(message).results().count();
        double specialRatio = (double) specialCount / message.length();
        if (specialRatio > 0.15) { // If more than 15% are special chars
            score += 45.0;
        }

        return Math.min(score, 100.0);
    }

    private String generateReason(String message, double score) {
        if (score < 60.0) {
            return "Message looks normal. Score: " + String.format("%.1f", score) + "%";
        }
        
        StringBuilder reason = new StringBuilder("Detected elements: ");
        String lowerMessage = message.toLowerCase();
        
        boolean hasKeywords = SPAM_KEYWORDS.stream().anyMatch(lowerMessage::contains);
        if (hasKeywords) reason.append("Suspicious keywords. ");
        
        if (URL_PATTERN.matcher(message).find()) reason.append("Contains links. ");
        
        long upperCount = UPPERCASE_PATTERN.matcher(message).results().count();
        if ((double) upperCount / message.length() > 0.3) reason.append("Excessive uppercase. ");
        
        long specialCount = SPECIAL_CHAR_PATTERN.matcher(message).results().count();
        if ((double) specialCount / message.length() > 0.15) reason.append("Excessive symbols. ");

        return reason.toString().trim();
    }
}
