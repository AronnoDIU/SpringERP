package com.springerp.repository;

import com.springerp.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findByTenantIdentifier(String tenantIdentifier);

    Optional<Tenant> findByTenantName(String tenantName);

    Optional<Tenant> findByAdminEmail(String adminEmail);
}

