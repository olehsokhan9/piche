package org.example.pichetesttask.controllers;

import org.example.pichetesttask.dto.AccountResponse;
import org.example.pichetesttask.dto.BasicAccountInfo;
import org.example.pichetesttask.dto.CreateAccountRequest;
import org.example.pichetesttask.dto.CreateAccountResponse;
import org.example.pichetesttask.services.AccountManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountManagementController {

    private final AccountManagementService accountManagementService;

    @Autowired
    public AccountManagementController(AccountManagementService accountManagementService) {
        this.accountManagementService = accountManagementService;
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest request) {
        final var account = accountManagementService.createAccount(request.amount(), request.currency());
        return ResponseEntity.ok(new CreateAccountResponse(account.id()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable UUID id) {
        final var account = accountManagementService.findAccount(id);
        return ResponseEntity.of(
            account.map(it -> new AccountResponse(
                it.id(),
                it.amount(),
                it.currency()
            ))
        );
    }

    @GetMapping
    public Collection<BasicAccountInfo> listAccounts() {
        return accountManagementService.findAllAccounts()
            .stream()
            .map(it -> new BasicAccountInfo(
                it.id(),
                it.amount(),
                it.currency()
            )).toList();
    }

}
