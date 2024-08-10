package kg.attractor.payment.service;

import kg.attractor.payment.dto.AccountDto;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    void createAccount(String currency);

    BigDecimal getBalance(String accountNumber);

    void addBalance(String accountNumber, BigDecimal money);

    List<AccountDto> getAccounts();
}
