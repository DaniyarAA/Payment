package kg.attractor.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    @GetMapping("/{accountId}/history")
    public ResponseEntity<?> getTransactionHistory(@PathVariable Long accountId) {
        return ResponseEntity.status(HttpStatus.OK).body("History...");
    }

    @PostMapping
    public ResponseEntity<?> performTransaction() {
        return ResponseEntity.ok("Transaction completed successfully");
    }
}

