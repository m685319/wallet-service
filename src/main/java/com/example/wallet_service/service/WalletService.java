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

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    @Transactional
    public Wallet createWallet() {
        Wallet wallet = new Wallet();
        wallet.setBalance(0L);
        return walletRepository.save(wallet);
    }

    @Transactional
    public WalletDTO deposit(UUID walletId, long amount) {
        Wallet wallet = findWalletById(walletId);
        wallet.setBalance(wallet.getBalance() + amount);
        Wallet updatedWallet = walletRepository.save(wallet);
        return walletMapper.toDTO(updatedWallet);
    }

    @Transactional
    public WalletDTO withdraw(UUID walletId, long amount) {
        Wallet wallet = findWalletById(walletId);
        if (wallet.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        wallet.setBalance(wallet.getBalance() - amount);
        Wallet updatedWallet = walletRepository.save(wallet);
        return walletMapper.toDTO(updatedWallet);
    }

    @Transactional
    public WalletDTO updateBalance(UUID walletId, String operationType, long amount) {
        Optional<Wallet> walletOptional = walletRepository.findById(walletId);

        if (walletOptional.isEmpty()) {
            throw new WalletNotFoundException("Wallet not found");
        }

        Wallet wallet = walletOptional.get();

        if (operationType.equalsIgnoreCase("DEPOSIT")) {
            wallet.setBalance(wallet.getBalance() + amount);
        } else if (operationType.equalsIgnoreCase("WITHDRAW")) {
            if (wallet.getBalance() >= amount) {
                wallet.setBalance(wallet.getBalance() - amount);
            } else {
                throw new InsufficientFundsException("Insufficient balance");
            }
        } else {
            throw new InvalidOperationException("Invalid operation type");
        }

        Wallet updatedWallet = walletRepository.save(wallet);

        return walletMapper.toDTO(updatedWallet);
    }

    public WalletDTO getBalance(UUID walletId) {
        Wallet wallet = findWalletById(walletId);
        return walletMapper.toDTO(wallet);
    }

    private Wallet findWalletById(UUID walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + walletId));
    }
}
