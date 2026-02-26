package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Report Template entity for creating business intelligence reports.
 */
@Entity
@Table(name = "report_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportTemplate extends BaseEntity {

    @Column(name = "template_name", nullable = false, unique = true)
    private String templateName;

    @Column(name = "template_code", nullable = false, unique = true)
    private String templateCode;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "report_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportType reportType; // FINANCIAL, OPERATIONAL, INVENTORY, HR, SALES, etc.

    @Column(name = "report_category")
    private String reportCategory;

    @Column(name = "template_definition", columnDefinition = "LONGTEXT")
    private String templateDefinition; // XML/JSON definition

    @Column(name = "display_format")
    @Enumerated(EnumType.STRING)
    private DisplayFormat displayFormat = DisplayFormat.TABLE; // TABLE, CHART, DASHBOARD, PDF

    @Column(name = "data_source_query", columnDefinition = "LONGTEXT")
    private String dataSourceQuery;

    @Column(name = "refresh_frequency")
    @Enumerated(EnumType.STRING)
    private RefreshFrequency refreshFrequency; // REALTIME, HOURLY, DAILY, WEEKLY, MONTHLY

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "is_scheduled", nullable = false)
    private Boolean isScheduled = false;

    @Column(name = "scheduled_recipients", columnDefinition = "LONGTEXT")
    private String scheduledRecipients; // JSON array of email addresses

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "version")
    private Integer version = 1;

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum ReportType {
        FINANCIAL, OPERATIONAL, INVENTORY, HR, SALES, PURCHASE, CUSTOMER, SUPPLIER, PAYROLL, ASSET
    }

    public enum DisplayFormat {
        TABLE, CHART, DASHBOARD, PDF, EXCEL, CSV
    }

    public enum RefreshFrequency {
        REALTIME, HOURLY, DAILY, WEEKLY, MONTHLY, QUARTERLY, ANNUALLY
    }
}

