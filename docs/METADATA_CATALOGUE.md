---
document_id: EPSS-META-001
title: Metadata Catalogue
version: 1.6
classification: Public Portfolio Documentation
purpose: Explain business meaning without publishing proprietary implementation detail
---

# Metadata Catalogue

## Transaction metadata

| Metafield | Meaning | Protection |
|---|---|---|
| transactionId | Internal transaction identifier | UUID |
| traceNumber | Demo trace reference | Synthetic only |
| maskedCardNumber | Display-safe card representation | Never store full live PAN |
| transactionType | Demonstration transaction category | Controlled vocabulary |
| amount | Transaction value | Demo data only |
| currency | ISO-style currency code | Example: ZAR |
| channel | Originating demo channel | ATM or POS |
| issuerBank | Simulated routing destination | Demo institution only |
| status | Processing outcome | Approved or declined |
| approvalCode | Generated demo approval reference | Not a production code |
| createdDate | Journal creation timestamp | Audit field |

## Routing metadata

| Metafield | Meaning | Protection |
|---|---|---|
| binPrefix | Synthetic routing prefix | No production BIN catalogue |
| routeCode | Internal demo route label | No commercial interface detail |
| priority | Route selection order | Demonstration value |
| active | Route availability flag | Configuration control |

## Fraud metadata

| Metafield | Meaning | Protection |
|---|---|---|
| ruleCode | Fraud control identifier | Public control label only |
| reason | Decision reason | No production scoring model |
| riskScore | Demonstration severity value | Synthetic only |
| action | Resulting control action | Demo decline action |

## Settlement metadata

| Metafield | Meaning | Protection |
|---|---|---|
| batchReference | Settlement batch identifier | Synthetic |
| transactionCount | Included approved transactions | Derived metric |
| grossAmount | Batch monetary total | Demo data |
| issuerPosition | Aggregated simulated issuer total | Reconciliation view |
| settlementItem | Link to original approved transaction | Prevents double settlement |

## Management-reporting metadata

| Metafield | Definition |
|---|---|
| totalTransactions | Count of all journalled transactions |
| approvedTransactions | Count with approved status |
| declinedTransactions | Count with declined status |
| approvedAmount | Sum of approved transaction values |
| fraudAlerts | Count of persisted fraud alerts |
| settlementBatches | Count of settlement batches |
| settledAmount | Sum of completed batch values |
| approvalRate | Approved count divided by total count, expressed as a percentage |
