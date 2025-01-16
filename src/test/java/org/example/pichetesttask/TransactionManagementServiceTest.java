package org.example.pichetesttask;

import org.example.pichetesttask.model.Account;
import org.example.pichetesttask.model.Currency;
import org.example.pichetesttask.model.Transaction;
import org.example.pichetesttask.repository.AccountRepository;
import org.example.pichetesttask.repository.TransactionRepository;
import org.example.pichetesttask.services.TransactionManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.function.Consumer;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionManagementServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionTemplate transactionTemplate;

    @InjectMocks
    private TransactionManagementService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        doAnswer(invocation -> {
            Consumer<?> action = invocation.getArgument(0);
            action.accept(null); // Provide null as TransactionStatus
            return null;
        }).when(transactionTemplate).executeWithoutResult(any());
    }

    @Test
    void depositFunds_success() {
        // given
        var referenceId = randomUUID();
        var accountId = randomUUID();
        var currency = Currency.UAH;
        var amount = 100L;

        var account = new Account(accountId, 500L, currency);

        when(transactionRepository.existsByReferenceId(referenceId)).thenReturn(false);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        doAnswer(invocation -> {
            // Correctly mock the transaction template behavior
            Consumer<TransactionStatus> action = invocation.getArgument(0);
            action.accept(null);
            return null;
        }).when(transactionTemplate).executeWithoutResult(any());

        // when
        service.depositFunds(referenceId, accountId, amount, currency);

        // then
        verify(accountRepository).save(account);
        verify(transactionRepository, times(1)).save(any(Transaction.class));

        assertEquals(600L, account.amount());
    }

    @Test
    void withdrawFunds_insufficientBalance() {
        // given
        var referenceId = randomUUID();
        var accountId = randomUUID();
        var currency = Currency.UAH;
        var amount = 1000L;

        var account = new Account(accountId, 500L, currency);

        when(transactionRepository.existsByReferenceId(referenceId)).thenReturn(false);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // when
        var exception = assertThrows(IllegalArgumentException.class, () ->
                service.withdrawFunds(referenceId, accountId, amount, currency)
        );

        // then
        assertEquals("Not enough funds to withdraw", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(accountRepository, never()).save(account);
    }

    @Test
    void transferFunds_success() {
        // given
        var referenceId = randomUUID();
        var fromAccountId = randomUUID();
        var toAccountId = randomUUID();
        var currency = Currency.UAH;
        var amount = 200L;

        var fromAccount = new Account(fromAccountId, 500L, currency);
        var toAccount = new Account(toAccountId, 300L, currency);

        when(transactionRepository.existsByReferenceId(referenceId)).thenReturn(false);
        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toAccountId)).thenReturn(Optional.of(toAccount));

        // when
        service.transferFunds(referenceId, fromAccountId, toAccountId, amount, currency);

        // then
        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);
        verify(transactionRepository, times(2)).save(any());

        assertEquals(300L, fromAccount.amount());
        assertEquals(500L, toAccount.amount());
    }

    @Test
    void transferFunds_insufficientBalance() {
        // given
        var referenceId = randomUUID();
        var fromAccountId = randomUUID();
        var toAccountId = randomUUID();
        var currency = Currency.UAH;
        var amount = 600L;

        var fromAccount = new Account(fromAccountId, 500L, currency);
        var toAccount = new Account(toAccountId, 300L, currency);

        when(transactionRepository.existsByReferenceId(referenceId)).thenReturn(false);
        when(accountRepository.findById(fromAccountId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toAccountId)).thenReturn(Optional.of(toAccount));

        // when
        var exception = assertThrows(IllegalArgumentException.class, () ->
                service.transferFunds(referenceId, fromAccountId, toAccountId, amount, currency)
        );

        // then
        assertEquals("Not enough funds to transfer", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(accountRepository, never()).save(fromAccount);
        verify(accountRepository, never()).save(toAccount);
    }
}
