package com.springerp.controller.admin;

import com.springerp.entity.Company;
import com.springerp.entity.Role;
import com.springerp.service.CompanyManagementService;
import com.springerp.service.RbacService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Admin controller for role and permission management.
 * Access is restricted to users with the ADMIN role.
 */
@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoleController {

    private final RbacService rbacService;
    private final CompanyManagementService companyService;

    @GetMapping
    public String listRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam Long companyId,
            Model model) {

        Page<Role> roles = rbacService.getRolesByCompany(companyId, PageRequest.of(page, size));
        Company company = companyService.getCompany(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        model.addAttribute("roles", roles);
        model.addAttribute("company", company);
        model.addAttribute("pageTitle", "Roles Management");

        return "admin/role/list";
    }

    @GetMapping("/create")
    public String createForm(@RequestParam Long companyId, Model model) {
        Company company = companyService.getCompany(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        model.addAttribute("company", company);
        model.addAttribute("pageTitle", "Create New Role");

        return "admin/role/form";
    }

    @PostMapping
    public String create(
            @RequestParam Long companyId,
            @RequestParam String roleCode,
            @RequestParam String roleName,
            @RequestParam String description,
            RedirectAttributes redirectAttributes) {

        try {
            rbacService.createRole(roleCode, roleName, description, companyId);
            redirectAttributes.addFlashAttribute("message", "Role created successfully!");
            return "redirect:/admin/roles?companyId=" + companyId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating role: " + e.getMessage());
            return "redirect:/admin/roles/create?companyId=" + companyId;
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Role role = rbacService.getRole(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        model.addAttribute("role", role);
        model.addAttribute("permissions", rbacService.getRolePermissions(id));
        model.addAttribute("pageTitle", "Edit Role: " + role.getRoleName());

        return "admin/role/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @RequestParam String roleName,
            @RequestParam String description,
            RedirectAttributes redirectAttributes) {

        try {
            rbacService.updateRole(id, roleName, description);
            redirectAttributes.addFlashAttribute("message", "Role updated successfully!");
            return "redirect:/admin/roles?companyId=" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating role: " + e.getMessage());
            return "redirect:/admin/roles/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/permissions")
    public String managePermissions(
            @PathVariable Long id,
            @RequestParam(required = false) String[] permissions,
            RedirectAttributes redirectAttributes) {

        try {
            Role role = rbacService.getRole(id)
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            // Remove all existing permissions
            rbacService.getRolePermissions(id).forEach(p ->
                rbacService.removePermissionFromRole(id, p)
            );

            // Add new permissions
            if (permissions != null) {
                for (String permission : permissions) {
                    rbacService.assignPermissionToRole(id, permission);
                }
            }

            redirectAttributes.addFlashAttribute("message", "Permissions updated successfully!");
            return "redirect:/admin/roles?companyId=" + role.getCompany().getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating permissions: " + e.getMessage());
            return "redirect:/admin/roles/" + id + "/edit";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id,
                        RedirectAttributes redirectAttributes) {
        try {
            Role role = rbacService.getRole(id)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            Long companyId = role.getCompany().getId();

            rbacService.deleteRole(id);
            redirectAttributes.addFlashAttribute("message", "Role deleted successfully!");
            return "redirect:/admin/roles?companyId=" + companyId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting role: " + e.getMessage());
            return "redirect:/admin/roles/" + id + "/edit";
        }
    }
}

