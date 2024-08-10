package kg.attractor.payment.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;

}
