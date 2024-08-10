package kg.attractor.payment.dao;

import kg.attractor.payment.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate jdbcTemplate;


    public void createUser(User user){
        String checkPhoneNumberSql = "select count(*) from users WHERE phone_number = ?";
        Integer count = jdbcTemplate.queryForObject(checkPhoneNumberSql, Integer.class, user.getPhoneNumber());

        if (count != null && count > 0) {
            throw new IllegalArgumentException("Phone number already exists: " + user.getPhoneNumber());
        }
        String sql = "insert into users (name, surname, age, email, password, phone_number, avatar, account_type, enabled, authority_id ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getName(),
                user.getPassword(),
                user.getPhoneNumber(),
                user.getEnabled(),
                user.getAuthorityId()
        );
    }
}
