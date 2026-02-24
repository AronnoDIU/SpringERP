package com.springerp.repository;

import com.springerp.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByPermissionCode(String permissionCode);

    List<Permission> findByResource(String resource);

    List<Permission> findByResourceAndAction(String resource, String action);

    List<Permission> findByCategory(String category);

    List<Permission> findByIsSystemPermission(Boolean isSystemPermission);

    List<Permission> findByIsActive(Boolean isActive);

    boolean existsByResourceAndAction(String resource, String action);
}

