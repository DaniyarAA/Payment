package kg.attractor.payment.dao;

import kg.attractor.payment.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public void createAccount(Account account) {
        String sql = "INSERT INTO accounts (account_number, user_id, currency, balance) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,account.getAccountNumber(), account.getUserId(), account.getCurrency(), account.getBalance());
    }
}
