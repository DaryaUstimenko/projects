package com.example.wallet.model;

import com.example.wallet.entity.OperationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class WalletRequest {
    private UUID walletId;
    private OperationType operationType;
    @NotNull
    @Min(value = (long) 0.0, message = "Not less then 0.0")
    private double amount;
}
