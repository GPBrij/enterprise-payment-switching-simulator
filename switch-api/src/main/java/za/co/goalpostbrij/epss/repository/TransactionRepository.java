package za.co.goalpostbrij.epss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.goalpostbrij.epss.domain.Transaction;

import java.time.LocalDateTime;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    long countByCardNumberAndCreatedDateAfter(
            String cardNumber,
            LocalDateTime createdDate);
}
