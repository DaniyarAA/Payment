package kg.attractor.payment.service.impl;

import kg.attractor.payment.dao.AccountDao;
import kg.attractor.payment.dao.UserDao;
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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountDao accountDao;
    private final UserDao userDao;

    @Override
    public void createAccount(String currency) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .userId(userDao.getUserByPhoneNumber(auth.getName()).orElseThrow(
                        () -> new NotFoundUserException("User not found")
                ).getId())
                .currency(currency)
                .balance(BigDecimal.ZERO)
                .build();
        accountDao.createAccount(account);
    }

    public String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase();
    }

    @Override
    public BigDecimal getBalance(String accountNumber){
        Account account = accountDao.getByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundAccountException("Account not found"));
        return account.getBalance();
    }
}
