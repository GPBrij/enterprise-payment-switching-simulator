package za.co.goalpostbrij.epss.routing;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.co.goalpostbrij.epss.domain.RoutingRule;
import za.co.goalpostbrij.epss.repository.RoutingRuleRepository;

@Service
@RequiredArgsConstructor
public class RoutingService {

    private static final int BIN_LENGTH = 6;
    private final RoutingRuleRepository routingRuleRepository;

    public RoutingDecision route(String cardNumber) {
        String normalized = normalize(cardNumber);
        if (normalized.length() < BIN_LENGTH) {
            throw new IllegalArgumentException("Card number must contain at least 6 digits");
        }

        String binPrefix = normalized.substring(0, BIN_LENGTH);
        RoutingRule rule = routingRuleRepository
                .findFirstByBinPrefixAndActiveTrueOrderByPriorityAsc(binPrefix)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No active route configured for BIN: " + binPrefix));

        return new RoutingDecision(
                rule.getBinPrefix(),
                rule.getIssuerBank(),
                rule.getRouteCode());
    }

    public String mask(String cardNumber) {
        String normalized = normalize(cardNumber);
        if (normalized.length() < 10) {
            return "****";
        }
        return normalized.substring(0, 6)
                + "*".repeat(normalized.length() - 10)
                + normalized.substring(normalized.length() - 4);
    }

    private String normalize(String cardNumber) {
        if (cardNumber == null || cardNumber.isBlank()) {
            throw new IllegalArgumentException("Card number is required");
        }
        String normalized = cardNumber.replaceAll("[\\s-]", "");
        if (!normalized.matches("\\d+")) {
            throw new IllegalArgumentException("Card number must contain digits only");
        }
        return normalized;
    }
}
