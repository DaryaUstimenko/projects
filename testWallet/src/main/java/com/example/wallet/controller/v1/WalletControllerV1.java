package com.example.wallet.controller.v1;

import com.example.wallet.model.WalletRequest;
import com.example.wallet.model.WalletResponse;
import com.example.wallet.entity.WalletEntity;
import com.example.wallet.mapper.WalletMapper;
import com.example.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WalletControllerV1 {

    private final WalletService walletService;
    private final WalletMapper walletMapper;

    @PostMapping("/wallet")
    @ResponseStatus(HttpStatus.OK)
    public WalletResponse createWallet(@RequestBody WalletRequest walletRequest) {
        WalletEntity wallet = walletMapper.walletDtoToWalletEntity(walletRequest);
        return walletMapper.walletEntityToWalletResponse(walletService.save(wallet));
    }

    @GetMapping("/{id}")
    public WalletResponse getById(@PathVariable UUID id) {
        WalletResponse walletResponse = walletMapper.walletEntityToWalletResponse(
                walletService.findById(id).orElseThrow());
        return walletResponse;
    }

    @PostMapping("/wallet/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WalletResponse updateWallet(@PathVariable UUID id, @Valid @RequestBody WalletRequest request) {
        WalletResponse walletResponse = walletMapper
                .walletEntityToWalletResponse(walletService.updateWallet(id, request));

        return walletResponse;
    }

    @GetMapping("/wallets/{walletId}")
    @ResponseStatus(HttpStatus.OK)
    public WalletResponse getWalletBalance(@PathVariable UUID walletId) {
        return walletMapper.walletEntityToWalletResponse(walletService.getWalletBalance(walletId));
    }
}
