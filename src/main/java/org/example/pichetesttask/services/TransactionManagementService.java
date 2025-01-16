package org.example.pichetesttask.services;

import org.example.pichetesttask.model.Currency;
import org.example.pichetesttask.model.Transaction;
import org.example.pichetesttask.repository.AccountRepository;
import org.example.pichetesttask.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.example.pichetesttask.model.TransactionType.DEPOSIT;
import static org.example.pichetesttask.model.TransactionType.TRANSFER;
import static org.example.pichetesttask.model.TransactionType.WITHDRAW;

@Service
public class TransactionManagementService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    private final TransactionTemplate transactionTemplate;

    @Autowired
    public TransactionManagementService(TransactionRepository transactionRepository,
                                        AccountRepository accountRepository,
                                        TransactionTemplate transactionTemplate) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transactionTemplate = transactionTemplate;
    }

    public void depositFunds(UUID referenceId, UUID accountId, Long amount, Currency currency) {
        if (amount < 1) throw new IllegalArgumentException("amount should be greater than 0");
        if (transactionRepository.existsByReferenceId(referenceId)) return;

        transactionTemplate.executeWithoutResult(status -> {
            final var account = accountRepository.findById(accountId).orElseThrow();
            final var deposit = new Transaction(randomUUID(), amount, currency, accountId, DEPOSIT, referenceId);
            account.addAmount(deposit.amount());

            accountRepository.save(account);
            transactionRepository.save(deposit);
        });
    }

    public void withdrawFunds(UUID referenceId, UUID accountId, Long amount, Currency currency) {
        if (amount < 1) throw new IllegalArgumentException("amount should be greater than 0");
        if (transactionRepository.existsByReferenceId(referenceId)) return;

        transactionTemplate.executeWithoutResult(status -> {
            final var account = accountRepository.findById(accountId).orElseThrow();
            if (account.amount() < amount)
                throw new IllegalArgumentException("Not enough funds to withdraw");

            final var deposit = new Transaction(randomUUID(), -amount, currency, accountId, WITHDRAW, referenceId);
            account.addAmount(-amount);

            accountRepository.save(account);
            transactionRepository.save(deposit);
        });
    }

    public void transferFunds(UUID referenceId, UUID fromAccountId, UUID toAccountId, Long amount, Currency currency) {
        if (amount < 1) throw new IllegalArgumentException("amount should be greater than 0");
        if (transactionRepository.existsByReferenceId(referenceId)) return;

        transactionTemplate.executeWithoutResult(status -> {
            final var fromAccount = accountRepository.findById(fromAccountId).orElseThrow();
            if (fromAccount.amount() < amount)
                throw new IllegalArgumentException("Not enough funds to transfer");

            final var toAccount = accountRepository.findById(toAccountId).orElseThrow();

            final var transferFrom = new Transaction(randomUUID(), -amount, currency, fromAccountId, TRANSFER, referenceId);
            final var transferTo = new Transaction(randomUUID(), amount, currency, fromAccountId, TRANSFER, referenceId);
            fromAccount.addAmount(-amount);
            toAccount.addAmount(amount);

            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);
            transactionRepository.save(transferFrom);
            transactionRepository.save(transferTo);
        });
    }

}
