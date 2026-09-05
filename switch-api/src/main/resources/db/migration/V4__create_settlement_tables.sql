CREATE TABLE settlement_batches
(
    id UUID PRIMARY KEY,
    batch_reference VARCHAR(40) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    transaction_count INTEGER NOT NULL,
    gross_amount NUMERIC(18, 2) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    completed_date TIMESTAMP
);

CREATE TABLE settlement_positions
(
    id UUID PRIMARY KEY,
    batch_id UUID NOT NULL REFERENCES settlement_batches(id),
    issuer_bank VARCHAR(100) NOT NULL,
    transaction_count INTEGER NOT NULL,
    gross_amount NUMERIC(18, 2) NOT NULL,
    CONSTRAINT ux_settlement_position_batch_issuer
        UNIQUE (batch_id, issuer_bank)
);

CREATE TABLE settlement_items
(
    id UUID PRIMARY KEY,
    batch_id UUID NOT NULL REFERENCES settlement_batches(id),
    transaction_id UUID NOT NULL REFERENCES transactions(id),
    amount NUMERIC(18, 2) NOT NULL,
    issuer_bank VARCHAR(100) NOT NULL,
    CONSTRAINT ux_settlement_items_transaction UNIQUE (transaction_id)
);

CREATE INDEX ix_settlement_batches_created_date
    ON settlement_batches (created_date);

CREATE INDEX ix_settlement_items_batch_id
    ON settlement_items (batch_id);
