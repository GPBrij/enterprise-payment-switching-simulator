package za.co.goalpostbrij.epss.reporting;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.co.goalpostbrij.epss.dto.FraudMetricResponse;
import za.co.goalpostbrij.epss.dto.IssuerMetricResponse;
import za.co.goalpostbrij.epss.dto.OperationsSummaryResponse;
import za.co.goalpostbrij.epss.dto.SettlementMetricResponse;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OperationsReportingService {

    @PersistenceContext
    private EntityManager entityManager;

    public OperationsSummaryResponse summary() {
        Object[] row = (Object[]) entityManager.createNativeQuery("""
            SELECT
              COUNT(*) AS total_transactions,
              COUNT(*) FILTER (WHERE status = 'APPROVED') AS approved_transactions,
              COUNT(*) FILTER (WHERE status = 'DECLINED') AS declined_transactions,
              COALESCE(SUM(amount) FILTER (WHERE status = 'APPROVED'), 0) AS approved_amount,
              (SELECT COUNT(*) FROM fraud_alerts) AS fraud_alerts,
              (SELECT COUNT(*) FROM settlement_batches) AS settlement_batches,
              COALESCE((SELECT SUM(gross_amount) FROM settlement_batches WHERE status = 'COMPLETED'), 0) AS settled_amount
            FROM transactions
            """).getSingleResult();

        long total = number(row[0]).longValue();
        long approved = number(row[1]).longValue();
        double rate = total == 0 ? 0.0 : Math.round((approved * 10000.0) / total) / 100.0;

        return new OperationsSummaryResponse(
                total,
                approved,
                number(row[2]).longValue(),
                decimal(row[3]),
                number(row[4]).longValue(),
                number(row[5]).longValue(),
                decimal(row[6]),
                rate);
    }

    public List<IssuerMetricResponse> issuerMetrics() {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery("""
            SELECT issuer_bank, COUNT(*), COALESCE(SUM(amount), 0)
            FROM transactions
            GROUP BY issuer_bank
            ORDER BY COUNT(*) DESC, issuer_bank
            """).getResultList();
        return rows.stream()
                .map(r -> new IssuerMetricResponse(
                        String.valueOf(r[0]),
                        number(r[1]).longValue(),
                        decimal(r[2])))
                .toList();
    }

    public List<FraudMetricResponse> fraudMetrics() {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery("""
            SELECT rule_code, reason, COUNT(*), MAX(risk_score)
            FROM fraud_alerts
            GROUP BY rule_code, reason
            ORDER BY COUNT(*) DESC, rule_code
            """).getResultList();
        return rows.stream()
                .map(r -> new FraudMetricResponse(
                        String.valueOf(r[0]),
                        String.valueOf(r[1]),
                        number(r[2]).longValue(),
                        number(r[3]).intValue()))
                .toList();
    }

    public List<SettlementMetricResponse> settlementMetrics() {
        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery("""
            SELECT batch_reference, status, currency, transaction_count, gross_amount
            FROM settlement_batches
            ORDER BY created_date DESC
            LIMIT 10
            """).getResultList();
        return rows.stream()
                .map(r -> new SettlementMetricResponse(
                        String.valueOf(r[0]),
                        String.valueOf(r[1]),
                        String.valueOf(r[2]),
                        number(r[3]).intValue(),
                        decimal(r[4])))
                .toList();
    }

    private Number number(Object value) {
        return value == null ? 0 : (Number) value;
    }

    private BigDecimal decimal(Object value) {
        return value == null ? BigDecimal.ZERO : new BigDecimal(value.toString());
    }
}
