package org.example.pichetesttask.dto;

import org.example.pichetesttask.model.Currency;

import java.util.UUID;

public record BasicAccountInfo(
    UUID id,
    Long amount,
    Currency currency
) {
}
