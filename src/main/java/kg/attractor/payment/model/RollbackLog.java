package kg.attractor.payment.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RollbackLog {
    private Long id;
    private Long transactionId;
    private Long adminId;
    private String action;
    private LocalDateTime timestamp;
}
