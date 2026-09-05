---
document_id: EPSS-CAP-001
title: Capability Catalogue
version: 1.6
classification: Public Portfolio Documentation
---

# Capability Catalogue

| ID | Capability | Public description | Evidence |
|---|---|---|---|
| CAP-001 | Transaction acquisition | Accepts synthetic transaction requests | Transaction REST endpoint |
| CAP-002 | Transaction journal | Records transaction outcomes using masked card data | PostgreSQL transaction journal |
| CAP-003 | Routing | Selects a simulated issuer using synthetic BIN rules | Routing rules and issuer response |
| CAP-004 | Issuer simulation | Produces controlled approval and decline scenarios | Response code and message |
| CAP-005 | Fraud controls | Evaluates blocked-card, high-value and velocity scenarios | Fraud alert journal |
| CAP-006 | Settlement | Batches approved, unsettled transactions | Settlement batch |
| CAP-007 | Reconciliation | Aggregates positions by simulated issuer | Position totals equal batch totals |
| CAP-008 | Operations reporting | Aggregates transaction, fraud and settlement metrics | Operations REST endpoints |
| CAP-009 | Management dashboard | Displays KPIs, charts and oversight tables | React dashboard |
| CAP-010 | Release governance | Uses feature branches, commits and version tags | Git history |
