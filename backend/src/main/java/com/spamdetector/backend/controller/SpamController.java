package com.spamdetector.backend.controller;

import com.spamdetector.backend.dto.SpamCheckRequest;
import com.spamdetector.backend.dto.SpamCheckResponse;
import com.spamdetector.backend.security.CustomUserDetails;
import com.spamdetector.backend.service.SpamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spam")
@RequiredArgsConstructor
public class SpamController {

    private final SpamService spamService;

    @PostMapping("/check")
    public ResponseEntity<SpamCheckResponse> checkMessage(
            @RequestBody SpamCheckRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(spamService.checkMessage(request, userDetails.getUser()));
    }

    @GetMapping("/history")
    public ResponseEntity<List<SpamCheckResponse>> getHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(spamService.getHistory(userDetails.getUser()));
    }
}
