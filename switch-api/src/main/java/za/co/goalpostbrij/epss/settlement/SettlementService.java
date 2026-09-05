package za.co.goalpostbrij.epss.settlement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.goalpostbrij.epss.domain.SettlementBatch;
import za.co.goalpostbrij.epss.domain.SettlementItem;
import za.co.goalpostbrij.epss.domain.SettlementPosition;
import za.co.goalpostbrij.epss.domain.Transaction;
import za.co.goalpostbrij.epss.dto.SettlementBatchResponse;
import za.co.goalpostbrij.epss.dto.SettlementPositionResponse;
import za.co.goalpostbrij.epss.repository.SettlementBatchRepository;
import za.co.goalpostbrij.epss.repository.SettlementItemRepository;
import za.co.goalpostbrij.epss.repository.SettlementPositionRepository;
import za.co.goalpostbrij.epss.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final TransactionRepository transactionRepository;
    private final SettlementBatchRepository batchRepository;
    private final SettlementPositionRepository positionRepository;
    private final SettlementItemRepository itemRepository;

    @Transactional
    public SettlementBatchResponse createBatch() {
        List<Transaction> transactions =
                transactionRepository.findUnsettledApprovedTransactions();

        if (transactions.isEmpty()) {
            throw new IllegalStateException("No unsettled approved transactions are available");
        }

        String currency = validateSingleCurrency(transactions);
        UUID batchId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        String reference = "STL-" + now.format(
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + "-" + batchId.toString().substring(0, 8).toUpperCase();

        BigDecimal total = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        SettlementBatch batch = SettlementBatch.builder()
                .id(batchId)
                .batchReference(reference)
                .status("COMPLETED")
                .currency(currency)
                .transactionCount(transactions.size())
                .grossAmount(total)
                .createdDate(now)
                .completedDate(now)
                .build();
        batchRepository.save(batch);

        Map<String, List<Transaction>> byIssuer = new LinkedHashMap<>();
        for (Transaction transaction : transactions) {
            byIssuer.computeIfAbsent(
                    transaction.getIssuerBank(), key -> new java.util.ArrayList<>())
                    .add(transaction);

            itemRepository.save(SettlementItem.builder()
                    .id(UUID.randomUUID())
                    .batchId(batchId)
                    .transactionId(transaction.getId())
                    .amount(transaction.getAmount())
                    .issuerBank(transaction.getIssuerBank())
                    .build());
        }

        List<SettlementPositionResponse> positions = byIssuer.entrySet().stream()
                .map(entry -> createPosition(batchId, entry.getKey(), entry.getValue()))
                .toList();

        return toResponse(batch, positions);
    }

    @Transactional(readOnly = true)
    public SettlementBatchResponse getBatch(UUID batchId) {
        SettlementBatch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Settlement batch not found: " + batchId));

        List<SettlementPositionResponse> positions = positionRepository
                .findByBatchIdOrderByIssuerBank(batchId)
                .stream()
                .map(position -> new SettlementPositionResponse(
                        position.getIssuerBank(),
                        position.getTransactionCount(),
                        position.getGrossAmount()))
                .toList();

        return toResponse(batch, positions);
    }

    private SettlementPositionResponse createPosition(
            UUID batchId,
            String issuerBank,
            List<Transaction> transactions) {

        BigDecimal amount = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        SettlementPosition position = SettlementPosition.builder()
                .id(UUID.randomUUID())
                .batchId(batchId)
                .issuerBank(issuerBank)
                .transactionCount(transactions.size())
                .grossAmount(amount)
                .build();
        positionRepository.save(position);

        return new SettlementPositionResponse(
                issuerBank,
                transactions.size(),
                amount);
    }

    private String validateSingleCurrency(List<Transaction> transactions) {
        List<String> currencies = transactions.stream()
                .map(Transaction::getCurrency)
                .distinct()
                .toList();
        if (currencies.size() != 1) {
            throw new IllegalStateException(
                    "A settlement batch must contain one currency only");
        }
        return currencies.getFirst();
    }

    private SettlementBatchResponse toResponse(
            SettlementBatch batch,
            List<SettlementPositionResponse> positions) {
        return new SettlementBatchResponse(
                batch.getId(),
                batch.getBatchReference(),
                batch.getStatus(),
                batch.getCurrency(),
                batch.getTransactionCount(),
                batch.getGrossAmount(),
                batch.getCreatedDate(),
                batch.getCompletedDate(),
                positions);
    }
}
