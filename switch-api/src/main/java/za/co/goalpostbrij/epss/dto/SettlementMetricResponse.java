package za.co.goalpostbrij.epss.dto;

import java.math.BigDecimal;

public record SettlementMetricResponse(
        String batchReference,
        String status,
        String currency,
        int transactionCount,
        BigDecimal grossAmount
) {
}
