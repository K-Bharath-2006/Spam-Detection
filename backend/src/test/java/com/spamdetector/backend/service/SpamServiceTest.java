package com.spamdetector.backend.service;

import com.spamdetector.backend.dto.SpamCheckRequest;
import com.spamdetector.backend.dto.SpamCheckResponse;
import com.spamdetector.backend.entity.SpamCheck;
import com.spamdetector.backend.entity.User;
import com.spamdetector.backend.repository.SpamCheckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SpamServiceTest {

    @Mock
    private SpamCheckRepository spamCheckRepository;

    @InjectMocks
    private SpamService spamService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        when(spamCheckRepository.save(any(SpamCheck.class))).thenAnswer(invocation -> {
            SpamCheck sc = invocation.getArgument(0);
            sc.setId(100L);
            sc.setCheckedAt(LocalDateTime.now());
            return sc;
        });
    }

    @Test
    void testNormalMessage() {
        SpamCheckRequest request = new SpamCheckRequest("Hello team, lets have a meeting at 10 AM.");
        SpamCheckResponse response = spamService.checkMessage(request, testUser);

        assertEquals("NOT SPAM", response.getPrediction());
        assertTrue(response.getConfidence() <= 60.0);
    }

    @Test
    void testSpamMessageWithKeywordsAndLinks() {
        SpamCheckRequest request = new SpamCheckRequest("URGENT! You win a free lottery! Click now: http://spam.com");
        SpamCheckResponse response = spamService.checkMessage(request, testUser);

        assertEquals("SPAM", response.getPrediction());
        assertTrue(response.getConfidence() > 60.0);
    }

    @Test
    void testSpamMessageWithExcessiveUppercase() {
        SpamCheckRequest request = new SpamCheckRequest("URGENT HELLO THIS IS A VERY LOUD MESSAGE BUY NOW");
        SpamCheckResponse response = spamService.checkMessage(request, testUser);

        assertEquals("SPAM", response.getPrediction());
        assertTrue(response.getConfidence() >= 60.0);
    }

    @Test
    void testSpamMessageWithSpecialChars() {
        SpamCheckRequest request = new SpamCheckRequest("Buy $$$$$ !!!!!!!! @@@@@ click ####### free ***");
        SpamCheckResponse response = spamService.checkMessage(request, testUser);

        assertEquals("SPAM", response.getPrediction());
        assertTrue(response.getConfidence() > 60.0);
    }
}
