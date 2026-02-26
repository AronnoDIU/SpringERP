package com.springerp.repository;

import com.springerp.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Page<Notification> findByRecipientId(Long recipientId, Pageable pageable);

    Page<Notification> findByStatus(Notification.Status status, Pageable pageable);

    Page<Notification> findByChannel(Notification.Channel channel, Pageable pageable);

    Page<Notification> findByNotificationType(Notification.NotificationType type, Pageable pageable);

    List<Notification> findByRecipientIdAndStatusOrderByCreatedAtDesc(Long recipientId, Notification.Status status);

    long countByRecipientIdAndStatus(Long recipientId, Notification.Status status);

    Page<Notification> findByRelatedEntityTypeAndRelatedEntityId(String entityType, Long entityId, Pageable pageable);

    Page<Notification> findByTenantId(Long tenantId, Pageable pageable);
}

