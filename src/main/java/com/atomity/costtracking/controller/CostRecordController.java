package com.atomity.costtracking.controller;

import com.atomity.costtracking.dto.CostRecordResponse;
import com.atomity.costtracking.dto.CreateCostRecordRequest;
import com.atomity.costtracking.service.CostRecordService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cost-records")
public class CostRecordController {

    private final CostRecordService costRecordService;

    public CostRecordController(CostRecordService costRecordService) {
        this.costRecordService = costRecordService;
    }

    @PostMapping
    public ResponseEntity<CostRecordResponse> createCostRecord(@Valid @RequestBody CreateCostRecordRequest request) {
        CostRecordResponse response = costRecordService.createCostRecord(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CostRecordResponse>> listCostRecords(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<CostRecordResponse> records = costRecordService.listCostRecords(from, to);
        return ResponseEntity.ok(records);
    }
}
