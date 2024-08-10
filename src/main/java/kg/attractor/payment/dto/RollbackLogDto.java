package kg.attractor.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RollbackLogDto {
    private Long id;
    private Long transactionId;
    private Long adminId;
    private String action;
    private LocalDateTime timestamp;
}
