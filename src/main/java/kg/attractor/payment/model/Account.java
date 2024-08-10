package kg.attractor.payment.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Account {
    private Long id;
    private Long userId;
    private String currency;
    private BigDecimal balance;
}
