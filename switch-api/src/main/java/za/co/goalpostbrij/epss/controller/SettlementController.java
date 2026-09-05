package za.co.goalpostbrij.epss.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import za.co.goalpostbrij.epss.dto.SettlementBatchResponse;
import za.co.goalpostbrij.epss.settlement.SettlementService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @PostMapping("/batches")
    @ResponseStatus(HttpStatus.CREATED)
    public SettlementBatchResponse createBatch() {
        return settlementService.createBatch();
    }

    @GetMapping("/batches/{batchId}")
    public SettlementBatchResponse getBatch(@PathVariable UUID batchId) {
        return settlementService.getBatch(batchId);
    }
}
