package kg.attractor.payment.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String phoneNumber;
    private String name;
    private String password;
    private Boolean enabled;
    private Integer authorityId;
}
