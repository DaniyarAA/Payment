package kg.attractor.payment.controller;

import kg.attractor.payment.dto.SendMoneyDto;
import kg.attractor.payment.dto.TransactionDto;
import kg.attractor.payment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/{accountId}/history")
    public ResponseEntity<?> getTransactionHistory(@PathVariable Long accountId) {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getHistory(accountId));
    }

    @PostMapping
    public ResponseEntity<?> performTransaction(@RequestBody SendMoneyDto sendMoneyDto) {
        transactionService.makeTransaction(sendMoneyDto);
        return ResponseEntity.ok("Transaction completed successfully");
    }
}

