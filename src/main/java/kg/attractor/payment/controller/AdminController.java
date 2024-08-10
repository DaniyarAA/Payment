package kg.attractor.payment.controller;


import kg.attractor.payment.dto.TransactionDto;
import kg.attractor.payment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/transactions")
@RequiredArgsConstructor
public class AdminController {
    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/approval")
    public ResponseEntity<List<TransactionDto>> getPendingTransactions() {
        return ResponseEntity.ok(transactionService.getPendingTransactions());
    }

    //проверить
    @PostMapping("/approval")
    public ResponseEntity<?> approveTransaction(@RequestParam Long transactionId) {
        return ResponseEntity.ok("Transaction approved");
    }

    @PostMapping("/rollback")
    public ResponseEntity<?> rollbackTransaction() {
        return ResponseEntity.ok("Transaction rolled back");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable long id) {
        return ResponseEntity.ok("Transaction deleted");
    }


}
