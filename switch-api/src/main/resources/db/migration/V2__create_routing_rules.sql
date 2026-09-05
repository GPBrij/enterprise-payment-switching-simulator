CREATE TABLE routing_rules
(
    id UUID PRIMARY KEY,
    bin_prefix VARCHAR(8) NOT NULL,
    issuer_bank VARCHAR(100) NOT NULL,
    route_code VARCHAR(50) NOT NULL,
    priority INTEGER NOT NULL DEFAULT 100,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ux_routing_rules_bin_prefix UNIQUE (bin_prefix)
);

CREATE INDEX ix_routing_rules_active_priority
    ON routing_rules (active, priority);

INSERT INTO routing_rules
    (id, bin_prefix, issuer_bank, route_code, priority, active)
VALUES
    ('10000000-0000-0000-0000-000000000001', '400000', 'DEMO_BANK_A', 'ROUTE_A', 10, TRUE),
    ('10000000-0000-0000-0000-000000000002', '500000', 'DEMO_BANK_B', 'ROUTE_B', 20, TRUE),
    ('10000000-0000-0000-0000-000000000003', '600000', 'DEMO_BANK_C', 'ROUTE_C', 30, TRUE);
