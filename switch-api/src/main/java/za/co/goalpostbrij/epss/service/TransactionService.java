package za.co.goalpostbrij.epss.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.goalpostbrij.epss.domain.Transaction;
import za.co.goalpostbrij.epss.dto.TransactionRequest;
import za.co.goalpostbrij.epss.dto.TransactionResponse;
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
    private final IssuerSimulatorService issuerSimulatorService;

    @Transactional
    public TransactionResponse create(TransactionRequest request) {

        RoutingDecision routingDecision =
                routingService.route(request.cardNumber());

        AuthorizationDecision authorization =
                issuerSimulatorService.authorize(request.cardNumber());

        String approvalCode = authorization.status().equals("APPROVED")
                ? createApprovalCode()
                : null;

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .traceNumber(request.traceNumber())
                .cardNumber(routingService.mask(request.cardNumber()))
                .transactionType(request.transactionType())
                .amount(request.amount())
                .currency(request.currency())
                .channel(request.channel())
                .issuerBank(routingDecision.issuerBank())
                .status(authorization.status())
                .approvalCode(approvalCode)
                .createdDate(LocalDateTime.now())
                .build();

        repository.save(transaction);

        return new TransactionResponse(
                transaction.getStatus(),
                transaction.getApprovalCode(),
                transaction.getIssuerBank(),
                authorization.responseCode(),
                authorization.message()
        );
    }

    private String createApprovalCode() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6)
                .toUpperCase();
    }
}