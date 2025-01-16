package org.example.pichetesttask.dto;

import org.example.pichetesttask.model.Currency;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record TransferRequest(
    UUID referenceId,
    UUID fromAccountId,
    UUID toAccountId,
    Long amount,
    Currency currency
) {
    public TransferRequest {
        requireNonNull(referenceId);
        requireNonNull(fromAccountId);
        requireNonNull(toAccountId);
        requireNonNull(amount);
        requireNonNull(currency);
    }
}
