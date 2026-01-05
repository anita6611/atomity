package com.atomity.costtracking.repository;

import com.atomity.costtracking.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
    List<Team> findByTenantId(UUID tenantId);
    Optional<Team> findByIdAndTenantId(UUID id, UUID tenantId);
    boolean existsByTenantIdAndName(UUID tenantId, String name);
}
