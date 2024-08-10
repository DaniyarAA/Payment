package kg.attractor.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    @PostMapping
    public ResponseEntity<String> createAccount() {
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully");
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@RequestParam Long accountId) {
        return ResponseEntity.status(HttpStatus.OK).body("Balance...");
    }

    @PostMapping("/balance")
    public ResponseEntity<?> addBalance(@RequestParam Long accountId) {
        return ResponseEntity.status(HttpStatus.OK).body("Balance...");
    }


    @GetMapping
    public ResponseEntity<?> getUserAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body("Accounts...");
    }
}
