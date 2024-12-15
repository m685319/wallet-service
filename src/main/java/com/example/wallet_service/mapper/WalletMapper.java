package com.example.wallet_service.mapper;

import com.example.wallet_service.dto.WalletDTO;
import com.example.wallet_service.model.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    @Mapping(source = "id", target = "walletId")
    WalletDTO toDTO(Wallet wallet);
}
