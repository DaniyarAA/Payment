package kg.attractor.payment.dao;

import kg.attractor.payment.model.Account;
import kg.attractor.payment.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionDao {
    private final JdbcTemplate jdbcTemplate;

    public List<Transaction> getHistory(Long accountId) {
        String sql = "SELECT * FROM transactions WHERE sender_id = ?";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Transaction.class), accountId);
    }

    public void saveTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (sender_id, receiver_id, amount, status, created_at) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, transaction.getSenderId(), transaction.getReceiverId(), transaction.getAmount(),
                transaction.getStatus(),transaction.getCreatedAt());
    }

    public List<Transaction> getAllTransactions() {
        String sql = "SELECT * FROM transactions";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(Transaction.class));
    }
}
