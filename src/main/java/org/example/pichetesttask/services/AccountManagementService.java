package org.example.pichetesttask.services;

import org.example.pichetesttask.model.Account;
import org.example.pichetesttask.model.Currency;
import org.example.pichetesttask.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;

@Service
public class AccountManagementService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountManagementService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Long amount, Currency currency) {
        final var account = new Account(randomUUID(), amount, currency);
        return accountRepository.save(account);
    }

    public Optional<Account> findAccount(UUID id) {
        requireNonNull(id);
        return accountRepository.findById(id);
    }

    public Collection<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

}
