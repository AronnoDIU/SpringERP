package com.springerp.service;

import com.springerp.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service for RBAC (Role-Based Access Control) management.
 */
public interface RbacService {

    // Role Management
    Role createRole(String roleCode, String roleName, String description, Long companyId);
    Role updateRole(Long roleId, String roleName, String description);
    Optional<Role> getRole(Long roleId);
    Page<Role> getRolesByCompany(Long companyId, Pageable pageable);
    void deleteRole(Long roleId);

    // Permission Assignment
    void assignPermissionToRole(Long roleId, String permissionCode);
    void removePermissionFromRole(Long roleId, String permissionCode);
    List<String> getRolePermissions(Long roleId);

    // Permission Validation
    boolean hasPermission(Long userId, Long companyId, String resource, String action);
    boolean hasPermissionByCode(Long userId, Long companyId, String permissionCode);
    List<String> getUserPermissions(Long userId, Long companyId);

    // Role Assignment
    void assignRoleToUser(Long userId, Long companyId, Long roleId);
    void removeRoleFromUser(Long userId, Long companyId);
    Optional<Role> getUserRoleInCompany(Long userId, Long companyId);

    // Permission Matrix
    List<String> getAllPermissionsForCompany(Long companyId);
    List<String> getPermissionsByResource(String resource);

    // System Roles
    void initializeSystemRoles(Long companyId);
    boolean isSystemRole(Long roleId);

    // Role Statistics
    long countUsersWithRole(Long roleId);
}

