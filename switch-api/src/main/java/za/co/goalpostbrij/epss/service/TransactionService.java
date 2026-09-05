package za.co.goalpostbrij.epss.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.goalpostbrij.epss.domain.Transaction;
import za.co.goalpostbrij.epss.dto.TransactionRequest;
import za.co.goalpostbrij.epss.dto.TransactionResponse;
import za.co.goalpostbrij.epss.fraud.FraudDecision;
import za.co.goalpostbrij.epss.fraud.FraudEngineService;
import za.co.goalpostbrij.epss.issuer.AuthorizationDecision;
import za.co.goalpostbrij.epss.issuer.IssuerSimulatorService;
import za.co.goalpostbrij.epss.repository.TransactionRepository;
import za.co.goalpostbrij.epss.routing.RoutingDecision;
import za.co.goalpostbrij.epss.routing.RoutingService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final RoutingService routingService;
    private final FraudEngineService fraudEngineService;
    private final IssuerSimulatorService issuerSimulatorService;

    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        RoutingDecision routingDecision =
                routingService.route(request.cardNumber());

        FraudDecision fraudDecision =
                fraudEngineService.evaluate(request);

        if (fraudDecision.flagged()) {
            Transaction declinedTransaction = buildTransaction(
                    request,
                    routingDecision.issuerBank(),
                    "DECLINED",
                    null);

            repository.save(declinedTransaction);

            return new TransactionResponse(
                    declinedTransaction.getStatus(),
                    declinedTransaction.getApprovalCode(),
                    declinedTransaction.getIssuerBank(),
                    fraudDecision.responseCode(),
                    fraudDecision.reason());
        }

        AuthorizationDecision authorization =
                issuerSimulatorService.authorize(request.cardNumber());

        String approvalCode = authorization.status().equals("APPROVED")
                ? createApprovalCode()
                : null;

        Transaction transaction = buildTransaction(
                request,
                routingDecision.issuerBank(),
                authorization.status(),
                approvalCode);

        repository.save(transaction);

        return new TransactionResponse(
                transaction.getStatus(),
                transaction.getApprovalCode(),
                transaction.getIssuerBank(),
                authorization.responseCode(),
                authorization.message());
    }

    private Transaction buildTransaction(
            TransactionRequest request,
            String issuerBank,
            String status,
            String approvalCode) {

        return Transaction.builder()
                .id(UUID.randomUUID())
                .traceNumber(request.traceNumber())
                .cardNumber(routingService.mask(request.cardNumber()))
                .transactionType(request.transactionType())
                .amount(request.amount())
                .currency(request.currency())
                .channel(request.channel())
                .issuerBank(issuerBank)
                .status(status)
                .approvalCode(approvalCode)
                .createdDate(LocalDateTime.now())
                .build();
    }

    private String createApprovalCode() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6)
                .toUpperCase();
    }
}
