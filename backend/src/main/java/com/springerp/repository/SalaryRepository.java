package com.springerp.repository;

import com.springerp.entity.Salary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {

    Optional<Salary> findByEmployeeIdAndSalaryMonth(Long employeeId, LocalDate salaryMonth);

    Page<Salary> findByEmployeeId(Long employeeId, Pageable pageable);

    List<Salary> findByEmployeeIdAndSalaryMonthBetween(Long employeeId, LocalDate startMonth, LocalDate endMonth);

    Page<Salary> findBySalaryMonthBetween(LocalDate startMonth, LocalDate endMonth, Pageable pageable);

    Page<Salary> findByPaymentStatus(Salary.PaymentStatus status, Pageable pageable);

    List<Salary> findByPaymentStatusAndPaymentMethodNot(Salary.PaymentStatus status, Salary.PaymentMethod paymentMethod);

    Page<Salary> findByTenantId(Long tenantId, Pageable pageable);

    long countByPaymentStatus(Salary.PaymentStatus status);
}

