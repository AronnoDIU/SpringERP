package com.springerp.repository;

import com.springerp.entity.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    Page<StockMovement> findByInventoryItemId(Long inventoryItemId, Pageable pageable);

    Page<StockMovement> findByMovementType(StockMovement.MovementType movementType, Pageable pageable);

    Page<StockMovement> findByMovementDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<StockMovement> findByReferenceIdAndReferenceDocument(Long referenceId, String referenceDocument);

    Page<StockMovement> findByStatus(StockMovement.Status status, Pageable pageable);

    Page<StockMovement> findByApprovedBy(Long approvedBy, Pageable pageable);

    List<StockMovement> findByBatchNumber(String batchNumber);

    Page<StockMovement> findByTenantId(Long tenantId, Pageable pageable);
}

