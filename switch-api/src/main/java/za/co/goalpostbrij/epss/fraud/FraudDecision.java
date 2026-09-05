package za.co.goalpostbrij.epss.fraud;

public record FraudDecision(
        boolean flagged,
        String ruleCode,
        String reason,
        int riskScore,
        String responseCode
) {
    public static FraudDecision clear() {
        return new FraudDecision(false, "NONE", "CLEAR", 0, "00");
    }

    public static FraudDecision decline(
            String ruleCode,
            String reason,
            int riskScore) {
        return new FraudDecision(true, ruleCode, reason, riskScore, "59");
    }
}
