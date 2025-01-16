package org.example.pichetesttask.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "accounts")
@EntityListeners(AuditingEntityListener.class)
public class Account {

    @Id
    private UUID id;
    private Long amount;
    private Currency currency;
    @Version
    private Long version;
    @CreatedDate
    private Instant createdDate;
    @LastModifiedDate
    private Instant updatedDate;

    public Account() {}

    public Account(UUID id, Long amount, Currency currency) {
        this.id = requireNonNull(id);
        this.amount = requireNonNull(amount);
        this.currency = requireNonNull(currency);
    }

    public UUID id() {
        return id;
    }

    public Long amount() {
        return amount;
    }

    public void addAmount(Long amount) {
        this.amount = this.amount + amount;
    }

    public Currency currency() {
        return currency;
    }

    public Instant createdDate() {
        return createdDate;
    }
}
