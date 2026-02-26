package com.springerp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Notification entity for managing system notifications.
 * Supports multiple notification channels: email, SMS, in-app.
 */
@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_recipient_id", columnList = "recipient_id"),
        @Index(name = "idx_created_at", columnList = "created_at"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_notification_type", columnList = "notification_type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;

    @Column(name = "recipient_email")
    private String recipientEmail;

    @Column(name = "notification_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType; // INVOICE, ORDER, PAYMENT, SYSTEM, ALERT

    @Column(name = "channel", nullable = false)
    @Enumerated(EnumType.STRING)
    private Channel channel; // EMAIL, SMS, IN_APP, PUSH

    @Column(name = "subject")
    private String subject;

    @Column(name = "message", columnDefinition = "LONGTEXT")
    private String message;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING; // PENDING, SENT, FAILED, DELIVERED

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "related_entity_type")
    private String relatedEntityType; // INVOICE, ORDER, PAYMENT, etc.

    @Column(name = "related_entity_id")
    private Long relatedEntityId;

    @Column(name = "tenant_id")
    private Long tenantId;

    public enum NotificationType {
        INVOICE, ORDER, PAYMENT, SYSTEM, ALERT, APPROVAL, REMINDER
    }

    public enum Channel {
        EMAIL, SMS, IN_APP, PUSH, WEBHOOK
    }

    public enum Status {
        PENDING, SENT, DELIVERED, FAILED, BOUNCED, BLOCKED
    }
}

