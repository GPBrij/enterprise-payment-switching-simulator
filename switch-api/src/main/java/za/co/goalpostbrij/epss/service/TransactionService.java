package za.co.goalpostbrij.epss.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.co.goalpostbrij.epss.domain.Transaction;
import za.co.goalpostbrij.epss.dto.TransactionRequest;
import za.co.goalpostbrij.epss.dto.TransactionResponse;
import za.co.goalpostbrij.epss.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionResponse create(TransactionRequest request) {

        Transaction tx = Transaction.builder()
                .id(UUID.randomUUID())
                .traceNumber(request.traceNumber())
                .cardNumber(request.cardNumber())
                .transactionType(request.transactionType())
                .amount(request.amount())
                .currency(request.currency())
                .channel(request.channel())
                .issuerBank("DEMO_BANK")
                .status("APPROVED")
                .approvalCode("ABC123")
                .createdDate(LocalDateTime.now())
                .build();

        repository.save(tx);

        return new TransactionResponse(
                tx.getStatus(),
                tx.getApprovalCode(),
                tx.getIssuerBank()
        );
    }
}