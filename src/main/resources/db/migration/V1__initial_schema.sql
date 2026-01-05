-- Initial database schema for multi-tenant cost tracking
-- Creates tables: tenants, teams, cost_records
-- Enforces unique constraints and foreign key relationships

CREATE TABLE IF NOT EXISTS tenants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS teams (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_team_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id) ON DELETE CASCADE,
    CONSTRAINT uk_tenant_team_name UNIQUE (tenant_id, name)
);

CREATE INDEX idx_teams_tenant_id ON teams(tenant_id);

CREATE TABLE IF NOT EXISTS cost_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    team_id UUID NOT NULL,
    date DATE NOT NULL,
    cost_amount DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cost_record_team FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    CONSTRAINT uk_team_date UNIQUE (team_id, date)
);

CREATE INDEX idx_cost_records_team_id ON cost_records(team_id);
CREATE INDEX idx_cost_records_date ON cost_records(date);
CREATE INDEX idx_cost_records_team_date ON cost_records(team_id, date);
