package kg.attractor.payment.dao;

import kg.attractor.payment.model.Account;
import kg.attractor.payment.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public void createAccount(Account account) {
        String sql = "INSERT INTO accounts (account_number, user_id, currency, balance) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,account.getAccountNumber(), account.getUserId(), account.getCurrency(), account.getBalance());
    }

    public Optional<Account> getByAccountNumber(String accountNumber) {
        String sql = "SELECT balance FROM accounts WHERE account_number = ?";
        return Optional.ofNullable(
                DataAccessUtils.singleResult(jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Account.class),accountNumber))
        );
    }
}
