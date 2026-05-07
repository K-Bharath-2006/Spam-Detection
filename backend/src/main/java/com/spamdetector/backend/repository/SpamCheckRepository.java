package com.spamdetector.backend.repository;

import com.spamdetector.backend.entity.SpamCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpamCheckRepository extends JpaRepository<SpamCheck, Long> {
    List<SpamCheck> findByUserIdOrderByCheckedAtDesc(Long userId);
    long countByUserId(Long userId);
    long countByUserIdAndPrediction(Long userId, String prediction);
}
