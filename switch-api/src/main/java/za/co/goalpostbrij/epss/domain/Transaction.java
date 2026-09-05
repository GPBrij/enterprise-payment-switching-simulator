package za.co.goalpostbrij.epss.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    private UUID id;

    private String traceNumber;

    private String cardNumber;

    private String transactionType;

    private BigDecimal amount;

    private String currency;

    private String channel;

    private String issuerBank;

    private String status;

    private String approvalCode;

    private LocalDateTime createdDate;
}