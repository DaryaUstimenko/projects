package com.example.wallet.controller.v2;

import com.example.wallet.model.WalletRequest;
import com.example.wallet.entity.OperationType;
import com.example.wallet.entity.WalletEntity;
import com.example.wallet.mapper.WalletMapper;
import com.example.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WalletControllerV2 {

    private final WalletService walletService;
    private final WalletMapper walletMapper;

    @GetMapping(value = "/createForm", produces = MediaType.TEXT_HTML_VALUE)
    public String showCreateForm(Model model) {
        model.addAttribute("wallet", new WalletRequest());
        return "createWallet";
    }

    @PostMapping(value = "/wallet", produces = MediaType.TEXT_HTML_VALUE)
    public String createWallet(@ModelAttribute WalletRequest walletRequest, Model model) {

        WalletEntity walletEntity = walletService.save(walletMapper.walletDtoToWalletEntity(walletRequest));
        model.addAttribute("wallet", walletEntity);
        model.addAttribute("request", walletRequest);
        return "wallet";
    }


    @GetMapping(value = "/wallet/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String showUpdateForm(@PathVariable UUID id, Model model) {
        AtomicReference<String> showForm = new AtomicReference<>("");
        WalletEntity wallet = walletService.findById(id).orElseThrow();
        WalletRequest request = walletMapper.walletEntityToWalletRequest(wallet);
        if (wallet != null) {
            model.addAttribute("request", request);
            model.addAttribute("wallet", wallet);
            showForm.set("wallet");
        } else {
            showForm.set("redirect:/api/v1/createForm");
        }
        return showForm.get();
    }


    @PostMapping(value = "/wallet/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public String updateWallet(@PathVariable UUID id,
                               @Valid @ModelAttribute(name = "request") WalletRequest request, BindingResult result,
                               @RequestParam(value = "type", required = false, defaultValue = "deposit") String operationType,
                               Model model) {
        if (result.hasErrors()) {
            WalletEntity wallet = walletService.findById(id).orElseThrow();
            model.addAttribute("wallet", wallet);
            model.addAttribute("request", request);
            return "wallet";
        }
        request.setOperationType(OperationType.valueOf(operationType.toUpperCase(Locale.ROOT)));
        WalletEntity wallet = walletService.updateWallet(id, request);
        model.addAttribute("wallet", wallet);
        log.info("Wallet update" + wallet.toString());
        return "getBalance";
    }

    @GetMapping(value = "/wallets/{walletId}", produces = MediaType.TEXT_HTML_VALUE)
    public String getWalletBalance(@PathVariable UUID walletId, Model model) {
        walletService.getWalletBalance(walletId);
        WalletEntity wallet = walletService.findById(walletId).orElseThrow();
        model.addAttribute("wallet", wallet);
        return "getBalance";
    }
}
