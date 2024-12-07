package com.example.wallet_service.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WalletRateLimiter {

    private final ConcurrentHashMap<UUID, Bucket> walletBuckets = new ConcurrentHashMap<>();

    @Value("${app.rps.capacity}")
    private Integer capacity;

    @Value("${app.rps.refill-tokens}")
    private Integer refillTokens;

    @Value("${app.rps.refill-duration}")
    private Integer refillDuration;

    public boolean tryConsume(UUID walletId) {
        Bucket bucket = walletBuckets.computeIfAbsent(walletId, id -> createBucket());
        log.debug("Tokends remaining: {}", bucket.getAvailableTokens());
        return bucket.tryConsume(1);
    }

    private Bucket createBucket() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(capacity, Refill.intervally(refillTokens, Duration.ofSeconds(refillDuration))))
                .build();
    }
}