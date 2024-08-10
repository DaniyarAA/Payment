package kg.attractor.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendMoneyDto {
    @NotBlank
    private String senderAccountNumber;
    @NotBlank
    private String receiverAccountNumber;
    @NotNull
    @Positive
    private BigDecimal amount;
}
