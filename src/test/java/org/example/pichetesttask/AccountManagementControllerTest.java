package org.example.pichetesttask;

import org.example.pichetesttask.controllers.AccountManagementController;
import org.example.pichetesttask.dto.AccountResponse;
import org.example.pichetesttask.dto.BasicAccountInfo;
import org.example.pichetesttask.dto.CreateAccountRequest;
import org.example.pichetesttask.dto.CreateAccountResponse;
import org.example.pichetesttask.model.Account;
import org.example.pichetesttask.model.Currency;
import org.example.pichetesttask.services.AccountManagementService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AccountManagementControllerTest {

    @Mock
    private AccountManagementService accountManagementService;

    @InjectMocks
    private AccountManagementController accountManagementController;

    public AccountManagementControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() {
        // given
        CreateAccountRequest request = new CreateAccountRequest(1000L, Currency.UAH);
        UUID accountId = randomUUID();
        Account mockAccount = new Account(accountId, 1000L, Currency.UAH);

        when(accountManagementService.createAccount(request.amount(), request.currency())).thenReturn(mockAccount);

        // when
        ResponseEntity<?> response = accountManagementController.createAccount(request);

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof CreateAccountResponse);
        CreateAccountResponse responseBody = (CreateAccountResponse) response.getBody();
        assertEquals(accountId, responseBody.accountId());
        
        verify(accountManagementService, times(1)).createAccount(request.amount(), request.currency());
    }

    @Test
    void testGetAccount() {
        // given
        UUID accountId = randomUUID();
        Account mockAccount = new Account(accountId, 500L, Currency.UAH);

        when(accountManagementService.findAccount(accountId)).thenReturn(Optional.of(mockAccount));

        // when
        ResponseEntity<?> response = accountManagementController.getAccount(accountId);

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof AccountResponse);
        AccountResponse responseBody = (AccountResponse) response.getBody();
        assertEquals(accountId, responseBody.id());
        assertEquals(500L, responseBody.amount());
        assertEquals(Currency.UAH, responseBody.currency());

        verify(accountManagementService, times(1)).findAccount(accountId);
    }

    @Test
    void testGetAccountNotFound() {
        // given
        UUID accountId = randomUUID();

        when(accountManagementService.findAccount(accountId)).thenReturn(Optional.empty());

        // when
        ResponseEntity<?> response = accountManagementController.getAccount(accountId);

        // then
        assertEquals(404, response.getStatusCodeValue());
        verify(accountManagementService, times(1)).findAccount(accountId);
    }

    @Test
    void testListAccounts() {
        // given
        UUID accountId1 = randomUUID();
        UUID accountId2 = randomUUID();
        Account mockAccount1 = new Account(accountId1, 1000L, Currency.UAH);
        Account mockAccount2 = new Account(accountId2, 500L, Currency.UAH);

        when(accountManagementService.findAllAccounts()).thenReturn(List.of(mockAccount1, mockAccount2));

        // when
        List<BasicAccountInfo> accounts = (List<BasicAccountInfo>) accountManagementController.listAccounts();

        // then
        assertEquals(2, accounts.size());

        BasicAccountInfo accountInfo1 = accounts.get(0);
        assertEquals(accountId1, accountInfo1.id());
        assertEquals(1000L, accountInfo1.amount());
        assertEquals(Currency.UAH, accountInfo1.currency());

        BasicAccountInfo accountInfo2 = accounts.get(1);
        assertEquals(accountId2, accountInfo2.id());
        assertEquals(500L, accountInfo2.amount());
        assertEquals(Currency.UAH, accountInfo2.currency());

        verify(accountManagementService, times(1)).findAllAccounts();
    }
}
