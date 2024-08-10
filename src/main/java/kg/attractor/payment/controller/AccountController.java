package kg.attractor.payment.controller;

import kg.attractor.payment.dto.AccountDto;
import kg.attractor.payment.model.Account;
import kg.attractor.payment.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


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
    public ResponseEntity<?> getBalance(@RequestParam String accountNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getBalance(accountNumber));
    }

    @PostMapping("/balance")
    public ResponseEntity<?> addBalance(@RequestParam String accountNumber, @RequestParam BigDecimal money) {
        accountService.addBalance(accountNumber, money);
        return ResponseEntity.status(HttpStatus.OK).body("Balance updated successfully");
    }


    @GetMapping
    public ResponseEntity<List<AccountDto>> getUserAccounts() {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.getAccounts());
    }
}
