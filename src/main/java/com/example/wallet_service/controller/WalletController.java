package com.example.wallet_service.controller;

import com.example.wallet_service.dto.WalletDTO;
import com.example.wallet_service.service.WalletRateLimiter;
import com.example.wallet_service.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    private final WalletRateLimiter rateLimiter;

    @GetMapping("/{id}")
    public ResponseEntity<?> getWalletBalance(@PathVariable UUID id) {
        if (!rateLimiter.tryConsume(id)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
        return ResponseEntity.ok(walletService.getBalance(id));
    }

    @PostMapping
    public ResponseEntity<?> updateWallet(@RequestBody WalletDTO request) {
        if (!rateLimiter.tryConsume(request.getWalletId())) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit exceeded");
        }
        return ResponseEntity.ok(walletService.updateBalance(request));
    }

}