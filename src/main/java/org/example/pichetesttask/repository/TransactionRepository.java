package org.example.pichetesttask.repository;

import org.example.pichetesttask.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query(value = "SELECT EXISTS (SELECT 1 FROM transactions WHERE reference_id = :referenceId)", nativeQuery = true)
    boolean existsByReferenceId(@Param("referenceId") UUID referenceId);

}
