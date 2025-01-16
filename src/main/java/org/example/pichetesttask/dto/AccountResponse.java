package org.example.pichetesttask.dto;

import org.example.pichetesttask.model.Currency;

import java.util.UUID;

public record AccountResponse(
    UUID id,
    Long amount,
    Currency currency
) {
}
