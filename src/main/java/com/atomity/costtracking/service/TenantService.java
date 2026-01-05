package com.atomity.costtracking.service;

import com.atomity.costtracking.dto.CreateTenantRequest;
import com.atomity.costtracking.dto.TenantResponse;
import com.atomity.costtracking.exception.DuplicateResourceException;
import com.atomity.costtracking.model.Tenant;
import com.atomity.costtracking.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Transactional
    public TenantResponse createTenant(CreateTenantRequest request) {
        if (tenantRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Tenant with name '" + request.getName() + "' already exists");
        }

        Tenant tenant = new Tenant(request.getName());
        tenant = tenantRepository.save(tenant);

        return new TenantResponse(tenant.getId(), tenant.getName());
    }
}
