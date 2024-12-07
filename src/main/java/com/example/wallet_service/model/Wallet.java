package com.example.wallet_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Wallet {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private Long balance = 0L;
}
