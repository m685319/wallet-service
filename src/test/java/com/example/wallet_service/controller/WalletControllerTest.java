package com.example.wallet_service.controller;

import com.example.wallet_service.dto.WalletDTO;
import com.example.wallet_service.model.Wallet;
import com.example.wallet_service.repository.WalletRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WalletControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WalletRepository walletRepository;

    private UUID walletId;

    @BeforeEach
    void setUp() {
        Wallet wallet = new Wallet();
        wallet.setBalance(1000L);
        wallet = walletRepository.save(wallet);
        walletId = wallet.getId();
    }

    @Test
    void shouldReturnWalletBalance() {

        ResponseEntity<WalletDTO> response = restTemplate.getForEntity("/api/v1/wallets/{id}", WalletDTO.class, walletId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        WalletDTO walletDTO = response.getBody();
        assertThat(walletDTO).isNotNull();
        assertThat(walletDTO.getBalance()).isEqualTo(1000L);
    }

    @Test
    void shouldUpdateWalletBalanceWithDeposit() {

        WalletDTO request = new WalletDTO();
        request.setWalletId(walletId);
        request.setOperationType("DEPOSIT");
        request.setAmount(500L);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/wallets", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Operation completed successfully");

        Wallet updatedWallet = walletRepository.findById(walletId).orElseThrow();
        assertThat(updatedWallet.getBalance()).isEqualTo(1500L);
    }

    @Test
    void shouldUpdateWalletBalanceWithWithdraw() {

        WalletDTO request = new WalletDTO();
        request.setWalletId(walletId);
        request.setOperationType("WITHDRAW");
        request.setAmount(300L);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/wallets", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Operation completed successfully");

        Wallet updatedWallet = walletRepository.findById(walletId).orElseThrow();
        assertThat(updatedWallet.getBalance()).isEqualTo(700L);
    }
}