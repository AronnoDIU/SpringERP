package com.springerp.repository;

import com.springerp.entity.Company;
import com.springerp.entity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByNameContainingIgnoreCase(String name);

    Optional<Customer> findByEmail(String email);

    List<Customer> findByCompany(Company company);

    /**
     * Search customers by their associated company name (case-insensitive).
     */
    @Query("SELECT c FROM Customer c WHERE LOWER(c.company.name) LIKE LOWER(CONCAT('%', :companyName, '%'))")
    List<Customer> findByCompanyNameContainingIgnoreCase(@Param("companyName") String companyName);

    /**
     * Search customers by name or email, scoped to a company.
     */
    @EntityGraph(attributePaths = {"company"})
    @Query("SELECT c FROM Customer c WHERE c.company.id = :companyId " +
           "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Customer> searchByCompany(@Param("companyId") Long companyId, @Param("query") String query);

    List<Customer> findByIsDeletedFalse();
}
