package com.atomity.costtracking.service;

import com.atomity.costtracking.context.TenantContext;
import com.atomity.costtracking.dto.CostByTeamReportResponse;
import com.atomity.costtracking.dto.TeamCostBreakdown;
import com.atomity.costtracking.dto.TotalCostReportResponse;
import com.atomity.costtracking.model.CostRecord;
import com.atomity.costtracking.model.Team;
import com.atomity.costtracking.repository.CostRecordRepository;
import com.atomity.costtracking.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final CostRecordRepository costRecordRepository;
    private final TeamRepository teamRepository;

    public ReportService(CostRecordRepository costRecordRepository, TeamRepository teamRepository) {
        this.costRecordRepository = costRecordRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional(readOnly = true)
    public TotalCostReportResponse getTotalCostReport(LocalDate from, LocalDate to) {
        UUID tenantId = TenantContext.getTenantId();

        List<UUID> teamIds = teamRepository.findByTenantId(tenantId).stream()
                .map(Team::getId)
                .collect(Collectors.toList());

        BigDecimal totalCost = BigDecimal.ZERO;
        if (!teamIds.isEmpty()) {
            BigDecimal sum = costRecordRepository.sumCostByTeamIdsAndDateRange(teamIds, from, to);
            if (sum != null) {
                totalCost = sum;
            }
        }

        return new TotalCostReportResponse(tenantId, from, to, totalCost);
    }

    @Transactional(readOnly = true)
    public CostByTeamReportResponse getCostByTeamReport(LocalDate from, LocalDate to) {
        UUID tenantId = TenantContext.getTenantId();

        List<Team> teams = teamRepository.findByTenantId(tenantId);
        List<UUID> teamIds = teams.stream()
                .map(Team::getId)
                .collect(Collectors.toList());

        Map<UUID, String> teamIdToName = teams.stream()
                .collect(Collectors.toMap(Team::getId, Team::getName));

        List<TeamCostBreakdown> items = List.of();
        if (!teamIds.isEmpty()) {
            List<CostRecord> records = costRecordRepository.findByTeamIdsAndDateRange(teamIds, from, to);

            Map<UUID, BigDecimal> teamCosts = records.stream()
                    .collect(Collectors.groupingBy(
                            CostRecord::getTeamId,
                            Collectors.reducing(BigDecimal.ZERO, CostRecord::getCostAmount, BigDecimal::add)
                    ));

            items = teamCosts.entrySet().stream()
                    .map(entry -> new TeamCostBreakdown(entry.getKey(), teamIdToName.get(entry.getKey()), entry.getValue()))
                    .collect(Collectors.toList());
        }

        return new CostByTeamReportResponse(tenantId, from, to, items);
    }
}
