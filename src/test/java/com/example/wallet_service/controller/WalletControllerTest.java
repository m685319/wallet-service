package com.example.wallet_service.controller;

import com.example.wallet_service.model.Wallet;
import com.example.wallet_service.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private WalletRepository walletRepository;

    @Test
    void testUpdateBalance_depositSuccess() throws Exception {
        // when
        var jsonBody = """
                {
                    "walletId": "123e4567-e89b-12d3-a456-426614174000",
                    "operationType": "DEPOSIT",
                    "amount": 1000
                }
                """;

        // then
        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(2000))
                .andExpect(jsonPath("$.walletId").value("123e4567-e89b-12d3-a456-426614174000"));
        verify(walletRepository).findById(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        verify(walletRepository).save(any(Wallet.class));
    }

    @Test
    void testUpdateBalance_withdrawSuccess() throws Exception {
        String jsonBody = """
                {
                    "walletId": "123e4567-e89b-12d3-a456-426614174000",
                    "operationType": "WITHDRAW",
                    "amount": 500
                }
                """;

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(500))
                .andExpect(jsonPath("$.walletId").value("123e4567-e89b-12d3-a456-426614174000"));
    }

    @Test
    void testGetBalance_success() throws Exception {
        mockMvc.perform(get("/api/v1/wallets/{id}", "123e4567-e89b-12d3-a456-426614174000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1000));
    }

    @Test
    void testGetWallet_notFound() throws Exception {
        var nonExistWalletId = "910dda0b-8219-4325-85f5-dbc9c2f158e4";
        mockMvc.perform(get("/api/v1/wallets/{id}", nonExistWalletId))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Wallet not found: " + nonExistWalletId));
    }

    @Test
    void testInsufficientFunds_withdraw() throws Exception {
        String jsonBody = """
                {
                    "walletId": "123e4567-e89b-12d3-a456-426614174000",
                    "operationType": "WITHDRAW",
                    "amount": 1500
                }
                """;

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient balance"));
    }
}
