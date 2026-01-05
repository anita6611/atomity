package com.atomity.costtracking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CreateCostRecordRequest {

    @NotNull(message = "Team ID is required")
    private UUID teamId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Cost amount is required")
    @Positive(message = "Cost amount must be positive")
    private BigDecimal costAmount;

    public CreateCostRecordRequest() {
    }

    public CreateCostRecordRequest(UUID teamId, LocalDate date, BigDecimal costAmount) {
        this.teamId = teamId;
        this.date = date;
        this.costAmount = costAmount;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }
}
