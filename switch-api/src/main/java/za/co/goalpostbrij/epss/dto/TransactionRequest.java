package za.co.goalpostbrij.epss.dto;

import java.math.BigDecimal;

public record TransactionRequest(
        String traceNumber,
        String cardNumber,
        String transactionType,
        BigDecimal amount,
        String currency,
        String channel
) {
}