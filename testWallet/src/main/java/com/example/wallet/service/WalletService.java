package com.example.wallet.service;

import com.example.wallet.exception.InternalServerException;
import com.example.wallet.model.WalletRequest;
import com.example.wallet.entity.OperationType;
import com.example.wallet.entity.WalletEntity;
import com.example.wallet.exception.WalletException;
import com.example.wallet.exception.WalletNotFoundException;
import com.example.wallet.repositiry.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletEntity updateWallet(UUID walletId, WalletRequest walletRequest) {

        try {
            Optional<WalletEntity> walletOptional = walletRepository.findById(walletId);

            if (walletOptional.isEmpty()) {
                throw new WalletNotFoundException("Кошелек не найден", HttpStatus.NOT_FOUND);
            }

            WalletEntity wallet = walletOptional.get();

            if (walletRequest.getOperationType() == OperationType.DEPOSIT) {
                wallet.setBalance(wallet.getBalance() + walletRequest.getAmount());

            } else if (walletRequest.getOperationType() == OperationType.WITHDRAW) {
                if (wallet.getBalance() < walletRequest.getAmount()) {
                    throw new WalletException("Недостаточно средств на счете", HttpStatus.BAD_REQUEST);
                }
                wallet.setBalance(wallet.getBalance() - walletRequest.getAmount());
            }
            return walletRepository.save(wallet);
        } catch (Exception e) {
            log.error("Ошибка при обновлении кошелька: ", e);
            throw new InternalServerException("Внутренняя ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public WalletEntity getWalletBalance(UUID walletId) {
        try {
            Optional<WalletEntity> walletOptional = walletRepository.findById(walletId);
            if (walletOptional.isEmpty()) {
                throw new WalletNotFoundException("Кошелек не найден", HttpStatus.NOT_FOUND);
            }
            return walletRepository.findById(walletId).orElseThrow();
        } catch (Exception e) {
            log.error("Ошибка при получении баланса: ", e);
            throw new InternalServerException("Внутренняя ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public WalletEntity save(WalletEntity wallet) {
        try {
            return walletRepository.save(wallet);
        } catch (Exception e) {
            log.error("Ошибка при сохранении кошелька: ", e);
            throw new InternalServerException("Внутренняя ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Optional<WalletEntity> findById(UUID id) {
        try {
            return walletRepository.findById(id);
        } catch (Exception e) {
            log.error("Ошибка при поиске кошелька: ", e);
            throw new InternalServerException("Внутренняя ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
