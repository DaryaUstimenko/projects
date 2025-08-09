package com.example.wallet.mapper;

import com.example.wallet.model.WalletRequest;
import com.example.wallet.model.WalletResponse;
import com.example.wallet.entity.WalletEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
@Component
public class WalletMapper {

    public WalletResponse walletEntityToWalletResponse(WalletEntity wallet){
        return WalletResponse.builder()
                .walletId(wallet.getWalletId())
                .amount(wallet.getBalance())
                .build();
    }

    public WalletRequest walletEntityToWalletRequest(WalletEntity wallet){
        return WalletRequest.builder()
                .walletId(wallet.getWalletId())
                .amount(wallet.getBalance())
                .build();
    }

    public WalletEntity walletDtoToWalletEntity(WalletRequest walletDto) {
        return WalletEntity.builder()
                .balance(walletDto.getAmount())
                .build();
    }
}
