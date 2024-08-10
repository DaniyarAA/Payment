package kg.attractor.payment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Account {
    private Long id;
    private String accountNumber;
    private Long userId;
    private String currency;
    private BigDecimal balance;
}
