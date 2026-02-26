package com.springerp.repository;

import com.springerp.entity.AssetDepreciation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssetDepreciationRepository extends JpaRepository<AssetDepreciation, Long> {

    Page<AssetDepreciation> findByAssetId(Long assetId, Pageable pageable);

    List<AssetDepreciation> findByDepreciationDateBetween(LocalDate startDate, LocalDate endDate);

    List<AssetDepreciation> findByPeriod(String period);

    Page<AssetDepreciation> findByStatus(AssetDepreciation.Status status, Pageable pageable);

    List<AssetDepreciation> findByAssetIdAndStatus(Long assetId, AssetDepreciation.Status status);

    Page<AssetDepreciation> findByTenantId(Long tenantId, Pageable pageable);
}

