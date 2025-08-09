package com.example.wallet;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

import com.example.wallet.controller.v1.WalletControllerV1;
import com.example.wallet.entity.WalletEntity;
import com.example.wallet.mapper.WalletMapper;
import com.example.wallet.model.WalletRequest;
import com.example.wallet.model.WalletResponse;
import com.example.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class WalletControllerV1Test {

    private MockMvc mockMvc;

    @Mock
    private WalletService walletService;

    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private WalletControllerV1 walletControllerV1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(walletControllerV1).build();
    }

    @Test
    void createWallet_ShouldReturnWalletResponse() {
        WalletRequest walletRequest = new WalletRequest();
        WalletEntity walletEntity = new WalletEntity();
        WalletResponse walletResponse = new WalletResponse();

        when(walletMapper.walletDtoToWalletEntity(walletRequest)).thenReturn(walletEntity);
        when(walletService.save(walletEntity)).thenReturn(walletEntity);
        when(walletMapper.walletEntityToWalletResponse(walletEntity)).thenReturn(walletResponse);

        WalletResponse response = walletControllerV1.createWallet(walletRequest);

        assertNotNull(response);
        verify(walletMapper).walletDtoToWalletEntity(walletRequest);
        verify(walletService).save(walletEntity);
        verify(walletMapper).walletEntityToWalletResponse(walletEntity);
    }

    @Test
    void getById_ShouldReturnWalletResponse() {
        UUID walletId = UUID.randomUUID();
        WalletEntity walletEntity = new WalletEntity();
        WalletResponse walletResponse = new WalletResponse();

        when(walletService.findById(walletId)).thenReturn(Optional.of(walletEntity));
        when(walletMapper.walletEntityToWalletResponse(walletEntity)).thenReturn(walletResponse);

        WalletResponse response = walletControllerV1.getById(walletId);

        assertNotNull(response);
        verify(walletService).findById(walletId);
        verify(walletMapper).walletEntityToWalletResponse(walletEntity);
    }

    @Test
    void updateWallet_ShouldReturnUpdatedWalletResponse() {
        UUID walletId = UUID.randomUUID();
        WalletRequest walletRequest = new WalletRequest();
        WalletEntity walletEntity = new WalletEntity();
        WalletResponse walletResponse = new WalletResponse();

        when(walletService.updateWallet(walletId, walletRequest)).thenReturn(walletEntity);
        when(walletMapper.walletEntityToWalletResponse(walletEntity)).thenReturn(walletResponse);

        WalletResponse response = walletControllerV1.updateWallet(walletId, walletRequest);

        assertNotNull(response);
        verify(walletService).updateWallet(walletId, walletRequest);
        verify(walletMapper).walletEntityToWalletResponse(walletEntity);
    }

    @Test
    void getWalletBalance_ShouldReturnWalletResponse() {
        UUID walletId = UUID.randomUUID();
        WalletEntity walletEntity = new WalletEntity();
        WalletResponse walletResponse = new WalletResponse();

        when(walletService.getWalletBalance(walletId)).thenReturn(walletEntity);
        when(walletMapper.walletEntityToWalletResponse(walletEntity)).thenReturn(walletResponse);

        WalletResponse response = walletControllerV1.getWalletBalance(walletId);

        assertNotNull(response);
        verify(walletService).getWalletBalance(walletId);
        verify(walletMapper).walletEntityToWalletResponse(walletEntity);
    }
}