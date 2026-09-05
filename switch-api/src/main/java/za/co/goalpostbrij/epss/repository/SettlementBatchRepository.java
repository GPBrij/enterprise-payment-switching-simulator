package za.co.goalpostbrij.epss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.goalpostbrij.epss.domain.SettlementBatch;

import java.util.Optional;
import java.util.UUID;

public interface SettlementBatchRepository extends JpaRepository<SettlementBatch, UUID> {
    Optional<SettlementBatch> findByBatchReference(String batchReference);
}
