package com.example.wallet_service.controller;

import com.example.wallet_service.dto.WalletDTO;
import com.example.wallet_service.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/{id}")
    public WalletDTO getWalletBalance(@PathVariable UUID id) {
        return walletService.getBalance(id);
    }

    @PostMapping
    public String updateWallet(@RequestBody WalletDTO request) {
        walletService.updateBalance(request.getWalletId(), request.getOperationType(), request.getAmount());
        return "Operation completed successfully";
    }

}