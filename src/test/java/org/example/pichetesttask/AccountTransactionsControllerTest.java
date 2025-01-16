package org.example.pichetesttask;

import org.example.pichetesttask.controllers.AccountTransactionsController;
import org.example.pichetesttask.dto.DepositRequest;
import org.example.pichetesttask.dto.TransferRequest;
import org.example.pichetesttask.dto.WithdrawRequest;
import org.example.pichetesttask.services.TransactionManagementService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.example.pichetesttask.model.Currency.UAH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AccountTransactionsControllerTest {

    @Mock
    private TransactionManagementService transactionManagementService;

    @InjectMocks
    private AccountTransactionsController accountTransactionsController;

    public AccountTransactionsControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDepositFunds() {
        // given
        var referenceId = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var request = new DepositRequest(referenceId, accountId, 1000L, UAH);

        doNothing().when(transactionManagementService).depositFunds(
                request.referenceId(), request.accountId(), request.amount(), request.currency());

        // when
        ResponseEntity<?> response = accountTransactionsController.depositFunds(request);

        // then
        assertEquals(200, response.getStatusCodeValue());
        verify(transactionManagementService, times(1)).depositFunds(
                request.referenceId(), request.accountId(), request.amount(), request.currency());
    }

    @Test
    void testWithdrawFunds() {
        // given
        var referenceId = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var request = new WithdrawRequest(referenceId, accountId, 500L, UAH);

        doNothing().when(transactionManagementService).withdrawFunds(
                request.referenceId(), request.accountId(), request.amount(), request.currency());

        // when
        ResponseEntity<?> response = accountTransactionsController.withdrawFunds(request);

        // then
        assertEquals(200, response.getStatusCodeValue());
        verify(transactionManagementService, times(1)).withdrawFunds(
                request.referenceId(), request.accountId(), request.amount(), request.currency());
    }

    @Test
    void testTransferFunds() {
        // given
        var referenceId = UUID.randomUUID();
        var fromAccountId = UUID.randomUUID();
        var toAccountId = UUID.randomUUID();
        var request = new TransferRequest(referenceId, fromAccountId, toAccountId, 1500L, UAH);

        doNothing().when(transactionManagementService).transferFunds(
                request.referenceId(), request.fromAccountId(), request.toAccountId(), request.amount(), request.currency());

        // when
        ResponseEntity<?> response = accountTransactionsController.transferFunds(request);

        // then
        assertEquals(200, response.getStatusCodeValue());
        verify(transactionManagementService, times(1)).transferFunds(
                request.referenceId(), request.fromAccountId(), request.toAccountId(), request.amount(), request.currency());
    }
}
