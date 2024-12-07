package com.example.wallet_service.service;

import com.example.wallet_service.dto.WalletDTO;
import com.example.wallet_service.exception.InsufficientFundsException;
import com.example.wallet_service.exception.InvalidOperationException;
import com.example.wallet_service.exception.WalletNotFoundException;
import com.example.wallet_service.mapper.WalletMapper;
import com.example.wallet_service.model.Wallet;
import com.example.wallet_service.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    @Transactional
    public WalletDTO updateBalance(WalletDTO walletDTO) {
        Wallet wallet = findWalletById(walletDTO.getWalletId());
        switch (walletDTO.getOperationType()) {
            case "DEPOSIT" -> wallet.setBalance(wallet.getBalance() + walletDTO.getAmount());
            case "WITHDRAW" -> {
                if (wallet.getBalance() < walletDTO.getAmount()) {
                    throw new InsufficientFundsException("Insufficient balance");
                }
                wallet.setBalance(wallet.getBalance() - walletDTO.getAmount());
            }
            default -> throw new InvalidOperationException("Invalid operation type");
        }
        Wallet updatedWallet = walletRepository.save(wallet);
        return walletMapper.toDTO(updatedWallet);
    }

    public long getBalance(UUID walletId) {
        Wallet wallet = findWalletById(walletId);
        return wallet.getBalance();
    }

    private Wallet findWalletById(UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + walletId));
    }
}
