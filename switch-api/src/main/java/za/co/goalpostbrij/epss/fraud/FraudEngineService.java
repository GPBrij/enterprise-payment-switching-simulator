package za.co.goalpostbrij.epss.fraud;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.co.goalpostbrij.epss.domain.FraudAlert;
import za.co.goalpostbrij.epss.dto.TransactionRequest;
import za.co.goalpostbrij.epss.repository.FraudAlertRepository;
import za.co.goalpostbrij.epss.repository.TransactionRepository;
import za.co.goalpostbrij.epss.routing.RoutingService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FraudEngineService {

    private static final BigDecimal HIGH_AMOUNT_LIMIT = new BigDecimal("50000.00");
    private static final int VELOCITY_LIMIT = 3;
    private static final Set<String> BLOCKED_CARDS = Set.of(
            "4000009999999999",
            "5000009999999999",
            "6000009999999999"
    );

    private final TransactionRepository transactionRepository;
    private final FraudAlertRepository fraudAlertRepository;
    private final RoutingService routingService;

    public FraudDecision evaluate(TransactionRequest request) {
        validate(request);

        if (BLOCKED_CARDS.contains(normalize(request.cardNumber()))) {
            return recordAlert(request,
                    FraudDecision.decline(
                            "FRD-001",
                            "BLOCKED_CARD",
                            100));
        }

        if (request.amount().compareTo(HIGH_AMOUNT_LIMIT) > 0) {
            return recordAlert(request,
                    FraudDecision.decline(
                            "FRD-002",
                            "HIGH_AMOUNT_TRANSACTION",
                            90));
        }

        String maskedCard = routingService.mask(request.cardNumber());
        LocalDateTime windowStart = LocalDateTime.now().minusMinutes(1);
        long recentTransactions = transactionRepository
                .countByCardNumberAndCreatedDateAfter(maskedCard, windowStart);

        if (recentTransactions >= VELOCITY_LIMIT) {
            return recordAlert(request,
                    FraudDecision.decline(
                            "FRD-003",
                            "VELOCITY_LIMIT_EXCEEDED",
                            85));
        }

        return FraudDecision.clear();
    }

    private FraudDecision recordAlert(
            TransactionRequest request,
            FraudDecision decision) {

        FraudAlert alert = FraudAlert.builder()
                .id(UUID.randomUUID())
                .traceNumber(request.traceNumber())
                .maskedCardNumber(routingService.mask(request.cardNumber()))
                .ruleCode(decision.ruleCode())
                .reason(decision.reason())
                .riskScore(decision.riskScore())
                .action("DECLINE")
                .createdDate(LocalDateTime.now())
                .build();

        fraudAlertRepository.save(alert);
        return decision;
    }

    private void validate(TransactionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Transaction request is required");
        }
        if (request.amount() == null || request.amount().signum() <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero");
        }
    }

    private String normalize(String cardNumber) {
        if (cardNumber == null) {
            return "";
        }
        return cardNumber.replaceAll("[\\s-]", "");
    }
}
