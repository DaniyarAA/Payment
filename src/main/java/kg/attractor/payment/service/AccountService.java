package kg.attractor.payment.service;

import java.math.BigDecimal;

public interface AccountService {
    void createAccount(String currency);

    BigDecimal getBalance(String accountNumber);
}
