CREATE TABLE transactions
(
    id UUID PRIMARY KEY,
    trace_number VARCHAR(20) NOT NULL,
    card_number VARCHAR(32) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL,
    amount NUMERIC(18, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    issuer_bank VARCHAR(100),
    status VARCHAR(20) NOT NULL,
    approval_code VARCHAR(20),
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ux_transactions_trace_number
    ON transactions (trace_number);

CREATE INDEX ix_transactions_status
    ON transactions (status);

CREATE INDEX ix_transactions_created_date
    ON transactions (created_date);
