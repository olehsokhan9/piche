package org.example.pichetesttask;

import org.example.pichetesttask.model.Account;
import org.example.pichetesttask.model.Currency;
import org.example.pichetesttask.repository.AccountRepository;
import org.example.pichetesttask.services.AccountManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountManagementServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountManagementService accountManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_ShouldReturnSavedAccount() {
        // given
        var amount = 1000L;
        var currency = Currency.UAH;
        var account = new Account(UUID.randomUUID(), amount, currency);

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        // when
        var createdAccount = accountManagementService.createAccount(amount, currency);

        // then
        assertNotNull(createdAccount);
        assertEquals(amount, createdAccount.amount());
        assertEquals(currency, createdAccount.currency());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void findAccount_ShouldReturnAccountIfFound() {
        // given
        var id = UUID.randomUUID();
        var account = new Account(id, 500L, Currency.UAH);

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));

        // when
        var result = accountManagementService.findAccount(id);

        // then
        assertTrue(result.isPresent());
        assertEquals(account, result.get());
        verify(accountRepository, times(1)).findById(id);
    }

    @Test
    void findAccount_ShouldReturnEmptyIfNotFound() {
        // given
        var id = UUID.randomUUID();

        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        // when
        var result = accountManagementService.findAccount(id);

        // then
        assertFalse(result.isPresent());
        verify(accountRepository, times(1)).findById(id);
    }

    @Test
    void findAllAccounts_ShouldReturnAllAccounts() {
        // given
        var account1 = new Account(UUID.randomUUID(), 1000L, Currency.UAH);
        var account2 = new Account(UUID.randomUUID(), 2000L, Currency.UAH);
        List<Account> accounts = List.of(account1, account2);

        when(accountRepository.findAll()).thenReturn(accounts);

        // when
        Collection<Account> result = accountManagementService.findAllAccounts();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(account1));
        assertTrue(result.contains(account2));
        verify(accountRepository, times(1)).findAll();
    }
}