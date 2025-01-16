package org.example.pichetesttask.dto;

import org.example.pichetesttask.model.Currency;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record WithdrawRequest(
    UUID referenceId,
    UUID accountId,
    Long amount,
    Currency currency
) {
    public WithdrawRequest {
        requireNonNull(referenceId);
        requireNonNull(accountId);
        requireNonNull(amount);
        requireNonNull(currency);
    }
}
