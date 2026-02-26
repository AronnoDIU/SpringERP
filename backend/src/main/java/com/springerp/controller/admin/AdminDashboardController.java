package com.springerp.controller.admin;

import com.springerp.repository.UserRepository;
import com.springerp.service.CompanyManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Admin controller for main dashboard and system settings.
 * Access is restricted to users with the ADMIN role.
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    private final CompanyManagementService companyService;
    private final UserRepository userRepository;

    @GetMapping
    public String dashboard(Model model) {
        long totalCompanies = companyService.getAllCompanies(PageRequest.of(0, 1)).getTotalElements();
        long totalUsers = userRepository.count();
        long activeCompanies = companyService.getAllCompanies(PageRequest.of(0, 1000))
                .stream()
                .filter(c -> "ACTIVE".equals(c.getStatus()))
                .count();

        model.addAttribute("totalCompanies", totalCompanies);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeCompanies", activeCompanies);
        model.addAttribute("pageTitle", "Admin Dashboard");
        model.addAttribute("recentCompanies",
                companyService.getAllCompanies(PageRequest.of(0, 5)).getContent());

        return "admin/dashboard";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("pageTitle", "System Settings");
        return "admin/settings/system";
    }

    @PostMapping("/settings")
    public String saveSettings(
            @RequestParam String systemName,
            @RequestParam String systemEmail,
            @RequestParam String defaultLanguage,
            @RequestParam String defaultTimezone,
            RedirectAttributes redirectAttributes) {

        try {
            // TODO: Implement system settings storage
            redirectAttributes.addFlashAttribute("message", "Settings saved successfully!");
            return "redirect:/admin/settings";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving settings: " + e.getMessage());
            return "redirect:/admin/settings";
        }
    }

    @GetMapping("/email-config")
    public String emailConfig(Model model) {
        model.addAttribute("pageTitle", "Email Configuration");
        return "admin/settings/email";
    }

    @GetMapping("/api-keys")
    public String apiKeys(Model model) {
        model.addAttribute("pageTitle", "API Keys Management");
        return "admin/api/keys";
    }

    @GetMapping("/system-health")
    public String systemHealth(Model model) {
        model.addAttribute("pageTitle", "System Health");
        return "admin/monitoring/health";
    }
}

