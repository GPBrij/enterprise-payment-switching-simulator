package za.co.goalpostbrij.epss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.goalpostbrij.epss.domain.FraudAlert;

import java.util.UUID;

public interface FraudAlertRepository extends JpaRepository<FraudAlert, UUID> {
}
