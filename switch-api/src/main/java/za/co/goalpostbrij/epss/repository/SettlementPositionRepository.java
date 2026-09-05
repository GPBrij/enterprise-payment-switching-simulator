package za.co.goalpostbrij.epss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.goalpostbrij.epss.domain.SettlementPosition;

import java.util.List;
import java.util.UUID;

public interface SettlementPositionRepository extends JpaRepository<SettlementPosition, UUID> {
    List<SettlementPosition> findByBatchIdOrderByIssuerBank(UUID batchId);
}
