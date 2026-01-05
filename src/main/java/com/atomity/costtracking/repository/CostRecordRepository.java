package com.atomity.costtracking.repository;

import com.atomity.costtracking.model.CostRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CostRecordRepository extends JpaRepository<CostRecord, UUID> {

    @Query("SELECT cr FROM CostRecord cr WHERE cr.teamId IN :teamIds AND cr.date BETWEEN :fromDate AND :toDate")
    List<CostRecord> findByTeamIdsAndDateRange(@Param("teamIds") List<UUID> teamIds,
                                                @Param("fromDate") LocalDate fromDate,
                                                @Param("toDate") LocalDate toDate);

    @Query("SELECT SUM(cr.costAmount) FROM CostRecord cr WHERE cr.teamId IN :teamIds AND cr.date BETWEEN :fromDate AND :toDate")
    BigDecimal sumCostByTeamIdsAndDateRange(@Param("teamIds") List<UUID> teamIds,
                                            @Param("fromDate") LocalDate fromDate,
                                            @Param("toDate") LocalDate toDate);

    boolean existsByTeamIdAndDate(UUID teamId, LocalDate date);
}
