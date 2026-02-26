package com.springerp.service.impl;

import com.springerp.entity.Notification;
import com.springerp.repository.NotificationRepository;
import com.springerp.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service implementation for notification management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification createNotification(Notification notification) {
        log.info("Creating notification for user: {}", notification.getRecipientId());
        notification.setStatus(Notification.Status.PENDING);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification sendNotification(Long notificationId) {
        log.info("Sending notification: {}", notificationId);
        return notificationRepository.findById(notificationId)
                .map(notification -> {
                    notification.setStatus(Notification.Status.SENT);
                    notification.setSentAt(LocalDateTime.now());
                    return notificationRepository.save(notification);
                })
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationId));
    }

    @Override
    @Async("notificationExecutor")
    public Notification sendNotificationAsync(Notification notification) {
        return sendNotification(notification.getId());
    }

    @Override
    public void sendBulkNotifications(List<Notification> notifications) {
        log.info("Sending {} bulk notifications", notifications.size());
        notifications.forEach(this::sendNotificationAsync);
    }

    @Override
    public Optional<Notification> getNotification(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public Page<Notification> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByRecipientId(userId, pageable);
    }

    @Override
    public Page<Notification> getUnreadNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByRecipientId(userId, pageable);
    }

    @Override
    public Page<Notification> getNotificationsByStatus(Notification.Status status, Pageable pageable) {
        return notificationRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<Notification> getNotificationsByChannel(Notification.Channel channel, Pageable pageable) {
        return notificationRepository.findByChannel(channel, pageable);
    }

    @Override
    public Page<Notification> getNotificationsByType(Notification.NotificationType type, Pageable pageable) {
        return notificationRepository.findByNotificationType(type, pageable);
    }

    @Override
    public Notification markAsRead(Long notificationId) {
        log.info("Marking notification as read: {}", notificationId);
        return notificationRepository.findById(notificationId)
                .map(notification -> {
                    notification.setStatus(Notification.Status.DELIVERED);
                    notification.setReadAt(LocalDateTime.now());
                    return notificationRepository.save(notification);
                })
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationId));
    }

    @Override
    public Notification markAsUnread(Long notificationId) {
        log.info("Marking notification as unread: {}", notificationId);
        return notificationRepository.findById(notificationId)
                .map(notification -> {
                    notification.setStatus(Notification.Status.PENDING);
                    notification.setReadAt(null);
                    return notificationRepository.save(notification);
                })
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationId));
    }

    @Override
    public void markMultipleAsRead(List<Long> notificationIds) {
        log.info("Marking {} notifications as read", notificationIds.size());
        notificationIds.forEach(this::markAsRead);
    }

    @Override
    public Notification createFromTemplate(String templateName, Long recipientId, Map<String, Object> variables) {
        log.info("Creating notification from template: {}", templateName);
        // Implementation would load template and replace variables
        return null;
    }

    @Override
    public Notification sendInvoiceNotification(Long invoiceId, Long recipientId) {
        log.info("Sending invoice notification for invoice: {}", invoiceId);
        Notification notification = Notification.builder()
                .recipientId(recipientId)
                .notificationType(Notification.NotificationType.INVOICE)
                .channel(Notification.Channel.EMAIL)
                .subject("Invoice #" + invoiceId + " requires attention")
                .relatedEntityType("INVOICE")
                .relatedEntityId(invoiceId)
                .build();
        return createNotification(notification);
    }

    @Override
    public Notification sendOrderNotification(Long orderId, Long recipientId) {
        log.info("Sending order notification for order: {}", orderId);
        Notification notification = Notification.builder()
                .recipientId(recipientId)
                .notificationType(Notification.NotificationType.ORDER)
                .channel(Notification.Channel.EMAIL)
                .subject("Order #" + orderId + " status updated")
                .relatedEntityType("ORDER")
                .relatedEntityId(orderId)
                .build();
        return createNotification(notification);
    }

    @Override
    public Notification sendApprovalNotification(Long workflowInstanceId, Long approverId) {
        log.info("Sending approval notification for workflow: {}", workflowInstanceId);
        Notification notification = Notification.builder()
                .recipientId(approverId)
                .notificationType(Notification.NotificationType.APPROVAL)
                .channel(Notification.Channel.IN_APP)
                .subject("Approval required for workflow #" + workflowInstanceId)
                .relatedEntityType("WORKFLOW")
                .relatedEntityId(workflowInstanceId)
                .build();
        return createNotification(notification);
    }

    @Override
    public Notification sendPaymentReminder(Long invoiceId, Long recipientId) {
        log.info("Sending payment reminder for invoice: {}", invoiceId);
        Notification notification = Notification.builder()
                .recipientId(recipientId)
                .notificationType(Notification.NotificationType.REMINDER)
                .channel(Notification.Channel.EMAIL)
                .subject("Payment reminder: Invoice #" + invoiceId + " is due soon")
                .relatedEntityType("INVOICE")
                .relatedEntityId(invoiceId)
                .build();
        return createNotification(notification);
    }

    @Override
    public void retryFailedNotifications() {
        log.info("Retrying failed notifications");
        Page<Notification> failedNotifications = notificationRepository.findByStatus(Notification.Status.FAILED, Pageable.unpaged());
        failedNotifications.forEach(notification -> {
            if (notification.getRetryCount() < 3) {
                notification.setRetryCount(notification.getRetryCount() + 1);
                sendNotificationAsync(notification);
            }
        });
    }

    @Override
    public void retryNotification(Long notificationId) {
        log.info("Retrying notification: {}", notificationId);
        notificationRepository.findById(notificationId)
                .ifPresent(notification -> {
                    notification.setRetryCount(notification.getRetryCount() + 1);
                    sendNotificationAsync(notification);
                });
    }

    @Override
    public void deleteOldNotifications(int daysOld) {
        log.info("Deleting notifications older than {} days", daysOld);
        // Implementation would delete old notifications based on retention policy
    }

    @Override
    public void deleteNotification(Long id) {
        log.info("Deleting notification: {}", id);
        notificationRepository.deleteById(id);
    }
}

