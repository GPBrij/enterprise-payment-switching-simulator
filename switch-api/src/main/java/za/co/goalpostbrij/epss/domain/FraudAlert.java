package za.co.goalpostbrij.epss.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "fraud_alerts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FraudAlert {

    @Id
    private UUID id;

    @Column(name = "trace_number", nullable = false, length = 20)
    private String traceNumber;

    @Column(name = "masked_card_number", nullable = false, length = 32)
    private String maskedCardNumber;

    @Column(name = "rule_code", nullable = false, length = 50)
    private String ruleCode;

    @Column(nullable = false, length = 150)
    private String reason;

    @Column(name = "risk_score", nullable = false)
    private Integer riskScore;

    @Column(nullable = false, length = 20)
    private String action;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
}
