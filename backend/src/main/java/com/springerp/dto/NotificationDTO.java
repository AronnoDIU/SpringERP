package com.springerp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for Notification
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private Long id;
    private Long recipientId;
    private String notificationType;
    private String channel;
    private String subject;
    private String message;
    private String status;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;
    private String relatedEntityType;
    private Long relatedEntityId;
}

