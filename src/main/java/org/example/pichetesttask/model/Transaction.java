package org.example.pichetesttask.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "transactions")
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    private UUID id;
    private UUID accountId;
    private Long amount;
    private Currency currency;
    private TransactionType transactionType;
    private UUID referenceId;
    @CreatedDate
    private Instant createdDate;
    @LastModifiedDate
    private Instant updatedDate;

    public Transaction() {}

    public Transaction(UUID id,
                       Long amount,
                       Currency currency,
                       UUID accountId,
                       TransactionType transactionType,
                       UUID referenceId) {
        this.id = requireNonNull(id);
        this.amount = requireNonNull(amount);
        this.currency = requireNonNull(currency);
        this.accountId = requireNonNull(accountId);
        this.transactionType = requireNonNull(transactionType);
        this.referenceId = requireNonNull(referenceId);
    }

    public UUID id() {
        return id;
    }

    public Long amount() {
        return amount;
    }

    public Currency currency() {
        return currency;
    }

    public UUID accountId() {
        return accountId;
    }

    public Instant createdDate() {
        return createdDate;
    }
}
