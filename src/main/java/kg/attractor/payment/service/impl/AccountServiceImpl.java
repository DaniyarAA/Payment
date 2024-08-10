package kg.attractor.payment.service.impl;

import kg.attractor.payment.dao.AccountDao;
import kg.attractor.payment.dao.UserDao;
import kg.attractor.payment.dto.AccountDto;
import kg.attractor.payment.errors.NotFoundAccountException;
import kg.attractor.payment.errors.NotFoundUserException;
import kg.attractor.payment.model.Account;
import kg.attractor.payment.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao;
    private final UserDao userDao;

    @Override
    public void createAccount(String currency) {
        if (!currency.equals("USD") && !currency.equals("KGS")) {
            throw new RuntimeException("Invalid currency");
        }
        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .userId(getCurrentUserId())
                .currency(currency)
                .balance(BigDecimal.ZERO)
                .build();
        accountDao.createAccount(account);
    }


    @Override
    public BigDecimal getBalance(String accountNumber){
        Account account = accountDao.getByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundAccountException("Account not found"));
        if (!account.getUserId().equals(getCurrentUserId())) {
            throw new IllegalArgumentException("You can't check someone else's account");
        }
        return account.getBalance();
    }


    @Override
    public void addBalance(String accountNumber, BigDecimal money){
        if (money.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Account account = accountDao.getByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundAccountException("Account not found"));

        if (!account.getUserId().equals(getCurrentUserId())) {
            throw new IllegalArgumentException("You cannot deposit to someone else's account");
        }

        account.setBalance(account.getBalance().add(money));
        accountDao.updateAccount(account);
    }


    @Override
    public List<AccountDto> getAccounts(){
        var list = accountDao.getAllAccounts();

        return list.stream()
                .map(e -> AccountDto.builder()
                        .accountNumber(e.getAccountNumber())
                        .currency(e.getCurrency())
                        .build())
                .toList();
    }


    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userDao.getUserByPhoneNumber(auth.getName()).orElseThrow(
                () -> new NotFoundUserException("User not found")
        ).getId();
    }

    public String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase();
    }
}
