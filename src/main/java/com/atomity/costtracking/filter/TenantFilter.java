package com.atomity.costtracking.filter;

import com.atomity.costtracking.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TenantFilter extends OncePerRequestFilter {

    private static final String TENANT_HEADER = "X-Tenant-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String path = request.getRequestURI();

            if (path.equals("/tenants") && request.getMethod().equals("POST")) {
                filterChain.doFilter(request, response);
                return;
            }

            String tenantIdHeader = request.getHeader(TENANT_HEADER);
            if (tenantIdHeader == null || tenantIdHeader.trim().isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Missing X-Tenant-Id header\"}");
                return;
            }

            try {
                UUID tenantId = UUID.fromString(tenantIdHeader);
                TenantContext.setTenantId(tenantId);
                filterChain.doFilter(request, response);
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Invalid X-Tenant-Id format\"}");
            }
        } finally {
            TenantContext.clear();
        }
    }
}
