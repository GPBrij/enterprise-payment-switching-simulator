package za.co.goalpostbrij.epss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.goalpostbrij.epss.domain.RoutingRule;

import java.util.Optional;
import java.util.UUID;

public interface RoutingRuleRepository extends JpaRepository<RoutingRule, UUID> {
    Optional<RoutingRule> findFirstByBinPrefixAndActiveTrueOrderByPriorityAsc(String binPrefix);
}
