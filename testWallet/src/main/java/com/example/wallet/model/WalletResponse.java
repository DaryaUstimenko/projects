package com.example.wallet.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class WalletResponse {
    private UUID walletId;
    @Positive
    private double amount;
}
