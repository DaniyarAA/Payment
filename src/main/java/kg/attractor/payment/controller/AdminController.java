package kg.attractor.payment.controller;


import kg.attractor.payment.dto.TransactionDto;
import kg.attractor.payment.errors.InsufficientFundsException;
import kg.attractor.payment.errors.NotFoundTransactionException;
import kg.attractor.payment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        transactionService.approveTransaction(transactionId);
        return ResponseEntity.ok("Transaction approved");
    }

    @PostMapping("/rollback")
    public ResponseEntity<?> rollbackTransaction(@RequestParam Long transactionId) {
        try {
            transactionService.rollBackTransaction(transactionId);
            return ResponseEntity.ok("Transaction rolled back successfully");
        } catch (InsufficientFundsException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        } catch (NotFoundTransactionException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while rolling back the transaction");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Transaction marked as deleted successfully");
    }


}
