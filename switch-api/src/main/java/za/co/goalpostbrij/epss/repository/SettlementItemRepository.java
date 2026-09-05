package za.co.goalpostbrij.epss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.goalpostbrij.epss.domain.SettlementItem;

import java.util.UUID;

public interface SettlementItemRepository extends JpaRepository<SettlementItem, UUID> {
}
