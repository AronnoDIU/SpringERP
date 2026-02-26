package com.springerp.service;

import com.springerp.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for notification management.
 */
public interface NotificationService {

    // Notification Creation
    Notification createNotification(Notification notification);
    Notification sendNotification(Long notificationId);
    Notification sendNotificationAsync(Notification notification);
    void sendBulkNotifications(List<Notification> notifications);

    // Notification Retrieval
    Optional<Notification> getNotification(Long id);
    Page<Notification> getUserNotifications(Long userId, Pageable pageable);
    Page<Notification> getUnreadNotifications(Long userId, Pageable pageable);
    Page<Notification> getNotificationsByStatus(Notification.Status status, Pageable pageable);
    Page<Notification> getNotificationsByChannel(Notification.Channel channel, Pageable pageable);
    Page<Notification> getNotificationsByType(Notification.NotificationType type, Pageable pageable);

    // Notification Status Updates
    Notification markAsRead(Long notificationId);
    Notification markAsUnread(Long notificationId);
    void markMultipleAsRead(List<Long> notificationIds);

    // Notification Templates
    Notification createFromTemplate(String templateName, Long recipientId, java.util.Map<String, Object> variables);
    Notification sendInvoiceNotification(Long invoiceId, Long recipientId);
    Notification sendOrderNotification(Long orderId, Long recipientId);
    Notification sendApprovalNotification(Long workflowInstanceId, Long approverId);
    Notification sendPaymentReminder(Long invoiceId, Long recipientId);

    // Retry Failed Notifications
    void retryFailedNotifications();
    void retryNotification(Long notificationId);

    // Notification Cleanup
    void deleteOldNotifications(int daysOld);
    void deleteNotification(Long id);
}

