package kg.attractor.payment.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/transactions")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping
    public ResponseEntity<?> getAllTransactions() {
        return ResponseEntity.ok("All transactions");
    }

    @GetMapping("/approval")
    public ResponseEntity<?> getPendingTransactions() {
        return ResponseEntity.ok("pending transactions");
    }

    @PostMapping("/approval")
    public ResponseEntity<?> approveTransaction() {
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
