package org.example.pichetesttask.dto;

import org.example.pichetesttask.model.Currency;

import static java.util.Objects.requireNonNull;

public record CreateAccountRequest(
    Long amount,
    Currency currency
) {
    public CreateAccountRequest {
        requireNonNull(amount);
        requireNonNull(currency);
    }
}
