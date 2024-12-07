package com.example.wallet_service.mapper;

import com.example.wallet_service.dto.WalletDTO;
import com.example.wallet_service.model.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletMapper INSTANCE = Mappers.getMapper(WalletMapper.class);

    @Mapping(source = "id", target = "walletId")
    WalletDTO toDTO(Wallet wallet);

    Wallet toEntity(WalletDTO walletDTO);
}
