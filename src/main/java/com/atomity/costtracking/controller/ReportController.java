package com.atomity.costtracking.controller;

import com.atomity.costtracking.dto.CostByTeamReportResponse;
import com.atomity.costtracking.dto.TotalCostReportResponse;
import com.atomity.costtracking.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/total")
    public ResponseEntity<TotalCostReportResponse> getTotalCostReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        TotalCostReportResponse response = reportService.getTotalCostReport(from, to);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-team")
    public ResponseEntity<CostByTeamReportResponse> getCostByTeamReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        CostByTeamReportResponse response = reportService.getCostByTeamReport(from, to);
        return ResponseEntity.ok(response);
    }
}
