package com.spamdetector.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpamCheckResponse {
    private Long id;
    private String message;
    private String prediction;
    private Double confidence;
    private String reason;
    private LocalDateTime checkedAt;
}
