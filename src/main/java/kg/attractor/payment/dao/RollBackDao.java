package kg.attractor.payment.dao;

import kg.attractor.payment.model.RollbackLog;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RollBackDao {
    private final JdbcTemplate jdbcTemplate;

    public void save(RollbackLog log) {
        String sql = "INSERT INTO transaction_logs (transaction_id, admin_id, action, timestamp) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, log.getTransactionId(), log.getAdminId(), log.getAction(), log.getTimestamp());
    }
}
