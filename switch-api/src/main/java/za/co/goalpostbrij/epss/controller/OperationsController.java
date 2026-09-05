package za.co.goalpostbrij.epss.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.goalpostbrij.epss.dto.FraudMetricResponse;
import za.co.goalpostbrij.epss.dto.IssuerMetricResponse;
import za.co.goalpostbrij.epss.dto.OperationsSummaryResponse;
import za.co.goalpostbrij.epss.dto.SettlementMetricResponse;
import za.co.goalpostbrij.epss.reporting.OperationsReportingService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/operations")
@RequiredArgsConstructor
public class OperationsController {

    private final OperationsReportingService reportingService;

    @GetMapping("/summary")
    public OperationsSummaryResponse summary() {
        return reportingService.summary();
    }

    @GetMapping("/issuers")
    public List<IssuerMetricResponse> issuers() {
        return reportingService.issuerMetrics();
    }

    @GetMapping("/fraud")
    public List<FraudMetricResponse> fraud() {
        return reportingService.fraudMetrics();
    }

    @GetMapping("/settlements")
    public List<SettlementMetricResponse> settlements() {
        return reportingService.settlementMetrics();
    }
}
