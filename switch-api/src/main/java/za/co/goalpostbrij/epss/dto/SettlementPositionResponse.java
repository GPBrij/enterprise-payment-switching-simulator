package za.co.goalpostbrij.epss.dto;

import java.math.BigDecimal;

public record SettlementPositionResponse(
        String issuerBank,
        long transactionCount,
        BigDecimal grossAmount
) {
}
