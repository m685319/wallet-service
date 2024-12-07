package com.example.wallet_service.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    @GetMapping("/{id}")
    public String getWalletBalance(@PathVariable String id) {

        return "Баланс для кошелька " + id + ": 0";
    }

    @PostMapping
    public String updateWallet(@RequestBody String request) {

        return "Операция выполнена: " + request;
    }
}