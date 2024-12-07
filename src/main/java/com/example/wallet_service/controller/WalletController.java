package com.example.wallet_service.controller;

import com.example.wallet_service.dto.WalletDTO;
import com.example.wallet_service.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @GetMapping("/{id}")
    public long getWalletBalance(@PathVariable UUID id) {
        return walletService.getBalance(id);
    }

    @PostMapping
    public WalletDTO updateWallet(@RequestBody WalletDTO request) {
        return walletService.updateBalance(request);
    }

}