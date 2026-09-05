package za.co.goalpostbrij.epss.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SettlementBatchResponse(
        UUID batchId,
        String batchReference,
        String status,
        String currency,
        int transactionCount,
        BigDecimal grossAmount,
        LocalDateTime createdDate,
        LocalDateTime completedDate,
        List<SettlementPositionResponse> positions
) {
}
