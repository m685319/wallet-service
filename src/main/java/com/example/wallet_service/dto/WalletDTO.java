package com.example.wallet_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
public class WalletDTO {

    @NotNull(message = "Wallet ID is mandatory.")
    private UUID walletId;

    private String operationType;

    @PositiveOrZero(message = "Amount must be non-negative.")
    private Long amount;

    @PositiveOrZero(message = "Balance must be zero or positive.")
    private Long balance;
}
