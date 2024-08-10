package kg.attractor.payment.controller;

import kg.attractor.payment.model.Account;
import kg.attractor.payment.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<String> createAccount(@RequestParam String currency) {
        accountService.createAccount(currency);
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
