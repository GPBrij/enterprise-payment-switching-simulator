package za.co.goalpostbrij.epss.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import za.co.goalpostbrij.epss.dto.TransactionRequest;
import za.co.goalpostbrij.epss.dto.TransactionResponse;
import za.co.goalpostbrij.epss.service.TransactionService;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService service;

    @PostMapping
    public TransactionResponse create(
            @RequestBody TransactionRequest request) {

        return service.create(request);
    }
}