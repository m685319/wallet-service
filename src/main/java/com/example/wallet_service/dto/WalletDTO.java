package com.example.wallet_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.UUID;

@Data
public class WalletDTO {

    @NotNull(message = "Wallet ID is mandatory")
    private UUID walletId;

    private String operationType;

    private Long amount;

    @PositiveOrZero(message = "Balance must be zero or positive")
    private Long balance;
}
