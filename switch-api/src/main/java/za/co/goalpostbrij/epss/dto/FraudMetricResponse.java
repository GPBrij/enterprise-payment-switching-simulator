package za.co.goalpostbrij.epss.dto;

public record FraudMetricResponse(
        String ruleCode,
        String reason,
        long alertCount,
        int highestRiskScore
) {
}
