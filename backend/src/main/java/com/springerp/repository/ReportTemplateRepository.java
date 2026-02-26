package com.springerp.repository;

import com.springerp.entity.ReportTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportTemplateRepository extends JpaRepository<ReportTemplate, Long> {

    Optional<ReportTemplate> findByTemplateCode(String templateCode);

    Optional<ReportTemplate> findByTemplateName(String templateName);

    Page<ReportTemplate> findByReportType(ReportTemplate.ReportType reportType, Pageable pageable);

    Page<ReportTemplate> findByReportCategory(String reportCategory, Pageable pageable);

    Page<ReportTemplate> findByIsActive(Boolean isActive, Pageable pageable);

    Page<ReportTemplate> findByOwnerId(Long ownerId, Pageable pageable);

    List<ReportTemplate> findByIsScheduledAndIsActive(Boolean isScheduled, Boolean isActive);

    Page<ReportTemplate> findByTenantId(Long tenantId, Pageable pageable);
}

