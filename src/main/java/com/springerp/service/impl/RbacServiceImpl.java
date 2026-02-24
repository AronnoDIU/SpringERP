package com.springerp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springerp.entity.CompanyUser;
import com.springerp.entity.Permission;
import com.springerp.entity.Role;
import com.springerp.repository.CompanyUserRepository;
import com.springerp.repository.PermissionRepository;
import com.springerp.repository.RoleRepository;
import com.springerp.service.RbacService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Implementation of RBAC service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RbacServiceImpl implements RbacService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CompanyUserRepository companyUserRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Role createRole(String roleCode, String roleName, String description, Long companyId) {
        log.info("Creating role: {} for company: {}", roleCode, companyId);

        Role role = Role.builder()
                .roleCode(roleCode)
                .roleName(roleName)
                .description(description)
                .permissions("[]")
                .isActive(true)
                .isSystemRole(false)
                .build();

        return roleRepository.save(role);
    }

    @Override
    public Role updateRole(Long roleId, String roleName, String description) {
        return roleRepository.findById(roleId)
                .map(role -> {
                    role.setRoleName(roleName);
                    role.setDescription(description);
                    return roleRepository.save(role);
                })
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));
    }

    @Override
    public Optional<Role> getRole(Long roleId) {
        return roleRepository.findById(roleId);
    }

    @Override
    public Page<Role> getRolesByCompany(Long companyId, Pageable pageable) {
        return roleRepository.findByCompanyId(companyId, pageable);
    }

    @Override
    public void deleteRole(Long roleId) {
        log.info("Deleting role: {}", roleId);
        roleRepository.deleteById(roleId);
    }

    @Override
    public void assignPermissionToRole(Long roleId, String permissionCode) {
        roleRepository.findById(roleId).ifPresent(role -> {
            try {
                List<String> permissions = Arrays.asList(
                        objectMapper.readValue(role.getPermissions(), String[].class)
                );
                permissions = new ArrayList<>(permissions);
                if (!permissions.contains(permissionCode)) {
                    permissions.add(permissionCode);
                    role.setPermissions(objectMapper.writeValueAsString(permissions));
                    roleRepository.save(role);
                }
            } catch (Exception e) {
                log.error("Error assigning permission to role", e);
            }
        });
    }

    @Override
    public void removePermissionFromRole(Long roleId, String permissionCode) {
        roleRepository.findById(roleId).ifPresent(role -> {
            try {
                List<String> permissions = Arrays.asList(
                        objectMapper.readValue(role.getPermissions(), String[].class)
                );
                permissions = new ArrayList<>(permissions);
                permissions.remove(permissionCode);
                role.setPermissions(objectMapper.writeValueAsString(permissions));
                roleRepository.save(role);
            } catch (Exception e) {
                log.error("Error removing permission from role", e);
            }
        });
    }

    @Override
    public List<String> getRolePermissions(Long roleId) {
        try {
            Optional<Role> roleOpt = roleRepository.findById(roleId);
            if (roleOpt.isEmpty()) {
                return new ArrayList<>();
            }

            Role role = roleOpt.get();
            try {
                String[] perms = objectMapper.readValue(role.getPermissions(), String[].class);
                List<String> result = new ArrayList<>(Arrays.asList(perms));
                return result;
            } catch (Exception e) {
                return new ArrayList<>();
            }
        } catch (Exception e) {
            log.error("Error getting role permissions", e);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean hasPermission(Long userId, Long companyId, String resource, String action) {
        return companyUserRepository.findByUserIdAndCompanyId(userId, companyId)
                .map(cu -> {
                    List<String> permissions = getRolePermissions(cu.getRole().getId());
                    String permissionCode = resource + ":" + action;
                    return permissions.contains(permissionCode);
                })
                .orElse(false);
    }

    @Override
    public boolean hasPermissionByCode(Long userId, Long companyId, String permissionCode) {
        return companyUserRepository.findByUserIdAndCompanyId(userId, companyId)
                .map(cu -> {
                    List<String> permissions = getRolePermissions(cu.getRole().getId());
                    return permissions.contains(permissionCode);
                })
                .orElse(false);
    }

    @Override
    public List<String> getUserPermissions(Long userId, Long companyId) {
        return companyUserRepository.findByUserIdAndCompanyId(userId, companyId)
                .map(cu -> getRolePermissions(cu.getRole().getId()))
                .orElse(new ArrayList<>());
    }

    @Override
    public void assignRoleToUser(Long userId, Long companyId, Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleId));

        companyUserRepository.findByUserIdAndCompanyId(userId, companyId)
                .ifPresent(cu -> {
                    cu.setRole(role);
                    companyUserRepository.save(cu);
                });
    }

    @Override
    public void removeRoleFromUser(Long userId, Long companyId) {
        companyUserRepository.findByUserIdAndCompanyId(userId, companyId)
                .ifPresent(companyUserRepository::delete);
    }

    @Override
    public Optional<Role> getUserRoleInCompany(Long userId, Long companyId) {
        return companyUserRepository.findByUserIdAndCompanyId(userId, companyId)
                .map(CompanyUser::getRole);
    }

    @Override
    public List<String> getAllPermissionsForCompany(Long companyId) {
        return permissionRepository.findAll().stream()
                .map(Permission::getPermissionCode)
                .toList();
    }

    @Override
    public List<String> getPermissionsByResource(String resource) {
        return permissionRepository.findByResource(resource).stream()
                .map(Permission::getPermissionCode)
                .toList();
    }

    @Override
    public void initializeSystemRoles(Long companyId) {
        // Create default system roles if they don't exist
        if (!roleRepository.existsByRoleCodeAndCompanyId("ADMIN", companyId)) {
            createRole("ADMIN", "Administrator", "Full system access", companyId);
        }
        if (!roleRepository.existsByRoleCodeAndCompanyId("MANAGER", companyId)) {
            createRole("MANAGER", "Manager", "Department/module management", companyId);
        }
        if (!roleRepository.existsByRoleCodeAndCompanyId("USER", companyId)) {
            createRole("USER", "User", "Standard user access", companyId);
        }
        if (!roleRepository.existsByRoleCodeAndCompanyId("VIEWER", companyId)) {
            createRole("VIEWER", "Viewer", "Read-only access", companyId);
        }
    }

    @Override
    public boolean isSystemRole(Long roleId) {
        return roleRepository.findById(roleId)
                .map(Role::getIsSystemRole)
                .orElse(false);
    }

    @Override
    public long countUsersWithRole(Long roleId) {
        return companyUserRepository.countByCompanyId(null); // TODO: implement proper count
    }
}

