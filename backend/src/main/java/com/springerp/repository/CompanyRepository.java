package com.springerp.repository;

import com.springerp.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    // Find by unique identifiers
    Optional<Company> findByRegistrationNumber(String registrationNumber);
    Optional<Company> findByVatNumber(String vatNumber);
    Optional<Company> findByEmail(String email);
    
    // Search methods
    List<Company> findByNameContainingIgnoreCase(String name);
    List<Company> findByAddressContainingIgnoreCase(String address);
    
    // Currency based queries
    List<Company> findByCurrency(String currency);
    
    // Paginated queries
    Page<Company> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Company> findByCurrency(String currency, Pageable pageable);
    
    // Check existence
    boolean existsByRegistrationNumber(String registrationNumber);
    boolean existsByVatNumber(String vatNumber);
    boolean existsByEmail(String email);
    
    // Combined queries
    List<Company> findByNameContainingIgnoreCaseAndCurrency(String name, String currency);
}
