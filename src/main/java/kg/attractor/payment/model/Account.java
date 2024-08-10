package kg.attractor.payment.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Long id;
    private String accountNumber;
    private Long userId;
    private String currency;
    private BigDecimal balance;
}
