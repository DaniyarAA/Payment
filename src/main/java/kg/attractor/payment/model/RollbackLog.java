package kg.attractor.payment.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RollbackLog {
    private Long id;
    private Long transactionId;
    private Long adminId;
    private String action;
    private LocalDateTime timestamp;
}
