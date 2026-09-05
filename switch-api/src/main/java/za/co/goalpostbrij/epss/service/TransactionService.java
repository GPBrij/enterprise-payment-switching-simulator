package za.co.goalpostbrij.epss.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.goalpostbrij.epss.domain.Transaction;
import za.co.goalpostbrij.epss.dto.TransactionRequest;
import za.co.goalpostbrij.epss.dto.TransactionResponse;
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

    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        RoutingDecision decision = routingService.route(request.cardNumber());

        Transaction tx = Transaction.builder()
                .id(UUID.randomUUID())
                .traceNumber(request.traceNumber())
                .cardNumber(routingService.mask(request.cardNumber()))
                .transactionType(request.transactionType())
                .amount(request.amount())
                .currency(request.currency())
                .channel(request.channel())
                .issuerBank(decision.issuerBank())
                .status("APPROVED")
                .approvalCode(createApprovalCode())
                .createdDate(LocalDateTime.now())
                .build();

        repository.save(tx);

        return new TransactionResponse(
                tx.getStatus(),
                tx.getApprovalCode(),
                tx.getIssuerBank());
    }

    private String createApprovalCode() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6)
                .toUpperCase();
    }
}
