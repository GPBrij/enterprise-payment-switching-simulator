CREATE TABLE fraud_alerts
(
    id UUID PRIMARY KEY,
    trace_number VARCHAR(20) NOT NULL,
    masked_card_number VARCHAR(32) NOT NULL,
    rule_code VARCHAR(50) NOT NULL,
    reason VARCHAR(150) NOT NULL,
    risk_score INTEGER NOT NULL,
    action VARCHAR(20) NOT NULL,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX ix_fraud_alerts_trace_number
    ON fraud_alerts (trace_number);

CREATE INDEX ix_fraud_alerts_rule_code
    ON fraud_alerts (rule_code);

CREATE INDEX ix_fraud_alerts_created_date
    ON fraud_alerts (created_date);
