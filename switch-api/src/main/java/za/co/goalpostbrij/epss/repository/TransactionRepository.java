package za.co.goalpostbrij.epss.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import za.co.goalpostbrij.epss.domain.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    long countByCardNumberAndCreatedDateAfter(
            String cardNumber,
            LocalDateTime createdDate);

    @Query(value = """
            SELECT t.*
            FROM transactions t
            WHERE t.status = 'APPROVED'
              AND NOT EXISTS (
                  SELECT 1
                  FROM settlement_items si
                  WHERE si.transaction_id = t.id
              )
            ORDER BY t.created_date
            """, nativeQuery = true)
    List<Transaction> findUnsettledApprovedTransactions();
}
