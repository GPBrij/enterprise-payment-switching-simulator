package za.co.goalpostbrij.epss.dto;

import java.math.BigDecimal;

public record OperationsSummaryResponse(
        long totalTransactions,
        long approvedTransactions,
        long declinedTransactions,
        BigDecimal approvedAmount,
        long fraudAlerts,
        long settlementBatches,
        BigDecimal settledAmount,
        double approvalRate
) {
}
