package com.springerp.repository;

import com.springerp.entity.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    Optional<Asset> findByAssetCode(String assetCode);

    Page<Asset> findByAssetType(Asset.AssetType assetType, Pageable pageable);

    Page<Asset> findByCategory(String category, Pageable pageable);

    List<Asset> findByResponsiblePersonId(Long personId);

    Page<Asset> findByAssetStatus(Asset.AssetStatus status, Pageable pageable);

    Page<Asset> findByLocation(String location, Pageable pageable);

    List<Asset> findByAssetStatusAndAssetType(Asset.AssetStatus status, Asset.AssetType assetType);

    Page<Asset> findByTenantId(Long tenantId, Pageable pageable);

    long countByAssetStatus(Asset.AssetStatus status);
}

