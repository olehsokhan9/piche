package org.example.pichetesttask.controllers;

import org.example.pichetesttask.dto.DepositRequest;
import org.example.pichetesttask.dto.TransferRequest;
import org.example.pichetesttask.dto.WithdrawRequest;
import org.example.pichetesttask.services.TransactionManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class AccountTransactionsController {

    private final TransactionManagementService transactionManagementService;

    @Autowired
    public AccountTransactionsController(TransactionManagementService transactionManagementService) {
        this.transactionManagementService = transactionManagementService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> depositFunds(@RequestBody DepositRequest request) {
        transactionManagementService.depositFunds(request.referenceId(), request.accountId(), request.amount(), request.currency());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawFunds(@RequestBody WithdrawRequest request) {
        transactionManagementService.withdrawFunds(request.referenceId(), request.accountId(), request.amount(), request.currency());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferFunds(@RequestBody TransferRequest request) {
        transactionManagementService.transferFunds(
            request.referenceId(),
            request.fromAccountId(),
            request.toAccountId(),
            request.amount(),
            request.currency()
        );
        return ResponseEntity.ok().build();
    }

}
