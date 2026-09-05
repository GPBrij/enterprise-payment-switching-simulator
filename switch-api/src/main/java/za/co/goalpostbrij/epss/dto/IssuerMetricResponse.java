package za.co.goalpostbrij.epss.dto;

import java.math.BigDecimal;

public record IssuerMetricResponse(
        String issuerBank,
        long transactionCount,
        BigDecimal grossAmount
) {
}
