---
document_id: EPSS-ARCH-001
title: Architecture Overview
version: 1.6
classification: Public Portfolio Documentation
viewpoints: context, container, processing-line, governance-line
---

# Architecture Overview

## Context

EPSS demonstrates the controlled journey of a synthetic card transaction from acquisition through routing, risk evaluation, simulated authorization, journaling, settlement and management reporting.

## Processing line

```mermaid
flowchart LR
    Source[ATM or POS] --> API[Switch API]
    API --> Route[Routing Decision]
    Route --> Fraud[Fraud Decision]
    Fraud --> Auth[Issuer Decision]
    Auth --> Journal[(Transaction Journal)]
    Journal --> Settle[Settlement Batch]
    Settle --> Report[Operations Metrics]
```

## Control line

```mermaid
flowchart LR
    Validate[Input Validation] --> Mask[Card Masking]
    Mask --> RouteControl[Active Route Control]
    RouteControl --> FraudControl[Fraud Rules]
    FraudControl --> AuthControl[Authorization Response]
    AuthControl --> Audit[Audit Evidence]
    Audit --> Reconcile[Settlement Reconciliation]
```

## Container view

```mermaid
flowchart TB
    Browser[Web Browser] --> UI[React Dashboard]
    UI --> API[Spring Boot API]
    Client[API Test Client] --> API
    API --> DB[(PostgreSQL)]
    Flyway[Flyway Migrations] --> DB
    Docker[Docker Compose] --> DB
```

## Public architecture boundary

The diagrams intentionally communicate responsibilities, interfaces and governance outcomes without exposing internal algorithms, production parameters, proprietary message maps, cryptographic procedures, or commercial switch configuration.
