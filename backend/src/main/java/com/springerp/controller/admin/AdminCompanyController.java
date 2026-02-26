package com.springerp.controller.admin;

import com.springerp.dto.CompanyDTO;
import com.springerp.entity.Company;
import com.springerp.service.CompanyManagementService;
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
 * Admin controller for company management.
 * Access is restricted to users with the ADMIN role.
 */
@Controller
@RequestMapping("/admin/companies")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminCompanyController {

    private final CompanyManagementService companyService;

    @GetMapping
    public String listCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            Model model) {

        Page<Company> companies;
        if (search != null && !search.isEmpty()) {
            companies = companyService.searchCompanies(search, PageRequest.of(page, size));
        } else {
            companies = companyService.getAllCompanies(PageRequest.of(page, size));
        }

        model.addAttribute("companies", companies);
        model.addAttribute("search", search);
        model.addAttribute("pageTitle", "Companies Management");

        return "admin/company/list";
    }

    @GetMapping("/{id}")
    public String viewCompany(@PathVariable Long id, Model model) {
        Company company = companyService.getCompany(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        model.addAttribute("company", company);
        model.addAttribute("activeUsers", companyService.getTotalActiveUsers(id));
        model.addAttribute("subsidiaries", companyService.getSubsidiaries(id));
        model.addAttribute("pageTitle", "Company: " + company.getCompanyName());

        return "admin/company/view";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        Page<Company> parentCompanies = companyService.getAllCompanies(PageRequest.of(0, 100));
        model.addAttribute("parentCompanies", parentCompanies);
        model.addAttribute("pageTitle", "Create New Company");

        return "admin/company/form";
    }

    @PostMapping
    public String create(@ModelAttribute CompanyDTO companyDTO,
                        @RequestParam(required = false) Long parentCompanyId,
                        RedirectAttributes redirectAttributes) {

        try {
            Company company = companyService.createCompany(companyDTO, parentCompanyId);
            redirectAttributes.addFlashAttribute("message", "Company created successfully!");
            return "redirect:/admin/companies/" + company.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating company: " + e.getMessage());
            return "redirect:/admin/companies/create";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Company company = companyService.getCompany(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        Page<Company> parentCompanies = companyService.getAllCompanies(PageRequest.of(0, 100));
        model.addAttribute("company", company);
        model.addAttribute("parentCompanies", parentCompanies);
        model.addAttribute("pageTitle", "Edit Company: " + company.getCompanyName());

        return "admin/company/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                        @ModelAttribute CompanyDTO companyDTO,
                        RedirectAttributes redirectAttributes) {

        try {
            companyService.updateCompany(id, companyDTO);
            redirectAttributes.addFlashAttribute("message", "Company updated successfully!");
            return "redirect:/admin/companies/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating company: " + e.getMessage());
            return "redirect:/admin/companies/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/activate")
    public String activate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            companyService.activateCompany(id);
            redirectAttributes.addFlashAttribute("message", "Company activated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error activating company: " + e.getMessage());
        }
        return "redirect:/admin/companies/" + id;
    }

    @PostMapping("/{id}/deactivate")
    public String deactivate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            companyService.deactivateCompany(id);
            redirectAttributes.addFlashAttribute("message", "Company deactivated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deactivating company: " + e.getMessage());
        }
        return "redirect:/admin/companies/" + id;
    }

    @PostMapping("/{id}/suspend")
    public String suspend(@PathVariable Long id,
                         @RequestParam String reason,
                         RedirectAttributes redirectAttributes) {
        try {
            companyService.suspendCompany(id, reason);
            redirectAttributes.addFlashAttribute("message", "Company suspended successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error suspending company: " + e.getMessage());
        }
        return "redirect:/admin/companies/" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            companyService.deleteCompany(id);
            redirectAttributes.addFlashAttribute("message", "Company deleted successfully!");
            return "redirect:/admin/companies";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting company: " + e.getMessage());
            return "redirect:/admin/companies/" + id;
        }
    }

    @GetMapping("/{id}/hierarchy")
    public String viewHierarchy(@PathVariable Long id, Model model) {
        Company company = companyService.getCompany(id)
                .orElseThrow(() -> new RuntimeException("Company not found"));

        model.addAttribute("company", company);
        model.addAttribute("hierarchy", companyService.getCompanyHierarchy(id));
        model.addAttribute("pageTitle", "Company Hierarchy: " + company.getCompanyName());

        return "admin/company/hierarchy";
    }
}

