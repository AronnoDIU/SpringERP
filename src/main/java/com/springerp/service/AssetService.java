package com.springerp.service;

import com.springerp.entity.Asset;
import com.springerp.entity.AssetDepreciation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for fixed asset management.
 */
public interface AssetService {

    // Asset Management
    Asset createAsset(Asset asset);
    Asset updateAsset(Long id, Asset asset);
    Optional<Asset> getAsset(Long id);
    Optional<Asset> getAssetByCode(String assetCode);
    Page<Asset> getAllAssets(Pageable pageable);
    Page<Asset> getAssetsByType(Asset.AssetType type, Pageable pageable);
    Page<Asset> getAssetsByStatus(Asset.AssetStatus status, Pageable pageable);
    Page<Asset> getAssetsByCategory(String category, Pageable pageable);
    List<Asset> getAssetsByResponsiblePerson(Long personId);
    List<Asset> getAssetsByLocation(String location);

    // Asset Depreciation
    AssetDepreciation recordDepreciation(AssetDepreciation depreciation);
    AssetDepreciation calculateAndRecordMonthlyDepreciation(Long assetId, String period);
    List<AssetDepreciation> calculateBulkMonthlyDepreciation(String period);
    Optional<AssetDepreciation> getDepreciation(Long id);
    Page<AssetDepreciation> getAssetDepreciationHistory(Long assetId, Pageable pageable);

    // Asset Valuation
    BigDecimal calculateBookValue(Long assetId);
    BigDecimal calculateAccumulatedDepreciation(Long assetId);
    BigDecimal getAssetDepreciationExpense(Long assetId, LocalDate startDate, LocalDate endDate);

    // Asset Disposal
    Asset disposeAsset(Long assetId, LocalDate disposalDate, String disposalMethod, BigDecimal disposalValue);

    // Asset Reporting
    List<Object[]> getAssetSchedule(LocalDate asOfDate);
    List<Object[]> getDepreciationSchedule(LocalDate asOfDate);
    BigDecimal getTotalAssetValue(LocalDate asOfDate);
    BigDecimal getTotalAccumulatedDepreciation(LocalDate asOfDate);

    void deleteAsset(Long id);
}

