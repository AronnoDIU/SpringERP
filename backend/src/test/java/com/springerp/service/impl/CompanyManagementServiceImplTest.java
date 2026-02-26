package com.springerp.service.impl;

import com.springerp.dto.CompanyDTO;
import com.springerp.entity.Company;
import com.springerp.repository.CompanyRepository;
import com.springerp.repository.CompanyUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyManagementServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CompanyUserRepository companyUserRepository;

    @InjectMocks
    private CompanyManagementServiceImpl service;

    private CompanyDTO dto;
    private Company savedCompany;

    @BeforeEach
    void setUp() {
        dto = CompanyDTO.builder()
                .companyName("Acme Corp")
                .companyCode("ACME")
                .registrationNumber("REG-001")
                .taxId("TAX-001")
                .address("123 Main St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("US")
                .phone("+1-555-0100")
                .email("contact@acme.com")
                .website("https://acme.com")
                .currency("USD")
                .subscriptionTier("PROFESSIONAL")
                .build();

        savedCompany = new Company();
        savedCompany.setId(1L);
        savedCompany.setCompanyName("Acme Corp");
        savedCompany.setCompanyCode("ACME");
        savedCompany.setStatus("ACTIVE");
        savedCompany.setCurrency("USD");
        savedCompany.setSubscriptionTier("PROFESSIONAL");
    }

    @Test
    void createCompany_persistsAllDtoFields() {
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);

        Company result = service.createCompany(dto, null);

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepository).save(captor.capture());
        Company persisted = captor.getValue();

        assertThat(persisted.getCompanyName()).isEqualTo("Acme Corp");
        assertThat(persisted.getCompanyCode()).isEqualTo("ACME");
        assertThat(persisted.getTaxId()).isEqualTo("TAX-001");
        assertThat(persisted.getAddress()).isEqualTo("123 Main St");
        assertThat(persisted.getCity()).isEqualTo("New York");
        assertThat(persisted.getCountry()).isEqualTo("US");
        assertThat(persisted.getEmail()).isEqualTo("contact@acme.com");
    }

    @Test
    void createCompany_setsStatusActiveOnCreation() {
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);

        service.createCompany(dto, null);

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void createCompany_defaultsToUsdWhenCurrencyIsNull() {
        dto.setCurrency(null);
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);

        service.createCompany(dto, null);

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepository).save(captor.capture());
        assertThat(captor.getValue().getCurrency()).isEqualTo("USD");
    }

    @Test
    void createCompany_defaultsToProfessionalTierWhenTierIsNull() {
        dto.setSubscriptionTier(null);
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);

        service.createCompany(dto, null);

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepository).save(captor.capture());
        assertThat(captor.getValue().getSubscriptionTier()).isEqualTo("PROFESSIONAL");
    }

    @Test
    void createCompany_preservesExplicitCurrencyAndTier() {
        dto.setCurrency("EUR");
        dto.setSubscriptionTier("ENTERPRISE");
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);

        service.createCompany(dto, null);

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepository).save(captor.capture());
        assertThat(captor.getValue().getCurrency()).isEqualTo("EUR");
        assertThat(captor.getValue().getSubscriptionTier()).isEqualTo("ENTERPRISE");
    }

    @Test
    void createCompany_withParentCompanyId_linksParent() {
        Company parent = new Company();
        parent.setId(10L);
        parent.setCompanyName("Parent Corp");

        when(companyRepository.findById(10L)).thenReturn(Optional.of(parent));
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);

        service.createCompany(dto, 10L);

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepository).save(captor.capture());
        assertThat(captor.getValue().getParentCompany()).isEqualTo(parent);
    }

    @Test
    void createCompany_withNonExistentParentId_throwsException() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createCompany(dto, 99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void createCompany_withNoParentId_doesNotLinkParent() {
        when(companyRepository.save(any(Company.class))).thenReturn(savedCompany);

        service.createCompany(dto, null);

        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepository).save(captor.capture());
        assertThat(captor.getValue().getParentCompany()).isNull();
        verify(companyRepository, never()).findById(any());
    }

    @Test
    void updateCompany_updatesAddressAndContactFields() {
        Company existing = new Company();
        existing.setId(1L);
        existing.setCompanyName("Old Name");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(companyRepository.save(any(Company.class))).thenAnswer(inv -> inv.getArgument(0));

        dto.setCompanyName("New Name");
        dto.setCity("Los Angeles");
        dto.setEmail("new@acme.com");
        Company result = service.updateCompany(1L, dto);

        assertThat(result.getCompanyName()).isEqualTo("New Name");
        assertThat(result.getCity()).isEqualTo("Los Angeles");
        assertThat(result.getEmail()).isEqualTo("new@acme.com");
    }

    @Test
    void updateCompany_withNonExistentId_throwsException() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateCompany(99L, dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getCompany_returnsCompanyWhenFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(savedCompany));

        Optional<Company> result = service.getCompany(1L);

        assertThat(result).isPresent().contains(savedCompany);
    }

    @Test
    void getCompany_returnsEmptyWhenNotFound() {
        when(companyRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Company> result = service.getCompany(999L);

        assertThat(result).isEmpty();
    }

    @Test
    void getAllCompanies_delegatesToRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Company> page = new PageImpl<>(List.of(savedCompany));
        when(companyRepository.findAll(pageable)).thenReturn(page);

        Page<Company> result = service.getAllCompanies(pageable);

        assertThat(result.getContent()).containsExactly(savedCompany);
        verify(companyRepository).findAll(pageable);
    }

    @Test
    void deleteCompany_callsRepositoryDelete() {
        service.deleteCompany(1L);

        verify(companyRepository).deleteById(1L);
    }

    @Test
    void activateCompany_setsStatusToActive() {
        savedCompany.setStatus("INACTIVE");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(savedCompany));
        when(companyRepository.save(any(Company.class))).thenAnswer(inv -> inv.getArgument(0));

        Company result = service.activateCompany(1L);

        assertThat(result.getStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void activateCompany_withNonExistentId_throwsException() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.activateCompany(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deactivateCompany_setsStatusToInactive() {
        savedCompany.setStatus("ACTIVE");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(savedCompany));
        when(companyRepository.save(any(Company.class))).thenAnswer(inv -> inv.getArgument(0));

        Company result = service.deactivateCompany(1L);

        assertThat(result.getStatus()).isEqualTo("INACTIVE");
    }

    @Test
    void deactivateCompany_withNonExistentId_throwsException() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deactivateCompany(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void suspendCompany_setsStatusToSuspended() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(savedCompany));
        when(companyRepository.save(any(Company.class))).thenAnswer(inv -> inv.getArgument(0));

        Company result = service.suspendCompany(1L, "Payment overdue");

        assertThat(result.getStatus()).isEqualTo("SUSPENDED");
    }

    @Test
    void suspendCompany_withNonExistentId_throwsException() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.suspendCompany(99L, "reason"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getCompanyHierarchy_returnsCompanyAndItsDirectSubsidiaries() {
        Company parent = new Company();
        parent.setId(1L);

        Company child1 = new Company();
        child1.setId(2L);
        child1.setParentCompany(parent);

        Company child2 = new Company();
        child2.setId(3L);
        child2.setParentCompany(parent);

        Company unrelated = new Company();
        unrelated.setId(4L);

        when(companyRepository.findAll()).thenReturn(List.of(parent, child1, child2, unrelated));

        List<Company> hierarchy = service.getCompanyHierarchy(1L);

        assertThat(hierarchy).containsExactlyInAnyOrder(parent, child1, child2);
        assertThat(hierarchy).doesNotContain(unrelated);
    }

    @Test
    void getCompanyHierarchy_returnsOnlyTheCompanyWhenNoSubsidiariesExist() {
        Company company = new Company();
        company.setId(5L);

        when(companyRepository.findAll()).thenReturn(List.of(company));

        List<Company> hierarchy = service.getCompanyHierarchy(5L);

        assertThat(hierarchy).containsExactly(company);
    }

    @Test
    void getSubsidiaries_returnsOnlyDirectChildren() {
        Company parent = new Company();
        parent.setId(1L);

        Company child = new Company();
        child.setId(2L);
        child.setParentCompany(parent);

        Company grandchild = new Company();
        grandchild.setId(3L);
        grandchild.setParentCompany(child);

        when(companyRepository.findAll()).thenReturn(List.of(parent, child, grandchild));

        List<Company> subsidiaries = service.getSubsidiaries(1L);

        assertThat(subsidiaries).containsExactly(child);
        assertThat(subsidiaries).doesNotContain(parent, grandchild);
    }

    @Test
    void getSubsidiaries_returnsEmptyListWhenNoChildren() {
        Company company = new Company();
        company.setId(1L);

        when(companyRepository.findAll()).thenReturn(List.of(company));

        List<Company> subsidiaries = service.getSubsidiaries(1L);

        assertThat(subsidiaries).isEmpty();
    }

    @Test
    void getParentChain_returnsFullChainFromChildToRoot() {
        Company root = new Company();
        root.setId(1L);

        Company middle = new Company();
        middle.setId(2L);
        middle.setParentCompany(root);

        Company leaf = new Company();
        leaf.setId(3L);
        leaf.setParentCompany(middle);

        when(companyRepository.findById(3L)).thenReturn(Optional.of(leaf));

        List<Company> chain = service.getParentChain(3L);

        assertThat(chain).containsExactly(leaf, middle, root);
    }

    @Test
    void getParentChain_returnsSingleElementForRootCompany() {
        Company root = new Company();
        root.setId(1L);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(root));

        List<Company> chain = service.getParentChain(1L);

        assertThat(chain).containsExactly(root);
    }

    @Test
    void getParentChain_returnsEmptyListWhenCompanyNotFound() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        List<Company> chain = service.getParentChain(99L);

        assertThat(chain).isEmpty();
    }

    @Test
    void updateCompanyConfig_persistsJsonConfig() {
        String configJson = "{\"maxUsers\": 50, \"features\": [\"HR\", \"ACCOUNTING\"]}";
        when(companyRepository.findById(1L)).thenReturn(Optional.of(savedCompany));
        when(companyRepository.save(any(Company.class))).thenAnswer(inv -> inv.getArgument(0));

        Company result = service.updateCompanyConfig(1L, configJson);

        assertThat(result.getConfig()).isEqualTo(configJson);
    }

    @Test
    void updateCompanyConfig_withNonExistentId_throwsException() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateCompanyConfig(99L, "{}"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getCompanyConfig_returnsStoredConfig() {
        String configJson = "{\"maxUsers\": 100}";
        savedCompany.setConfig(configJson);
        when(companyRepository.findById(1L)).thenReturn(Optional.of(savedCompany));

        String result = service.getCompanyConfig(1L);

        assertThat(result).isEqualTo(configJson);
    }

    @Test
    void getCompanyConfig_withNonExistentId_throwsException() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCompanyConfig(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getCompanyConfig_returnsNullWhenConfigNotSet() {
        Company noConfigCompany = new Company();
        ReflectionTestUtils.setField(noConfigCompany, "id", 2L);
        noConfigCompany.setConfig(null);
        when(companyRepository.findById(2L)).thenReturn(Optional.of(noConfigCompany));

        String result = service.getCompanyConfig(2L);

        assertThat(result).isNull();
    }

    @Test
    void getTotalActiveUsers_returnsZero() {
        assertThat(service.getTotalActiveUsers(1L)).isZero();
    }

    @Test
    void getTotalApiCalls_returnsZero() {
        assertThat(service.getTotalApiCalls(1L)).isZero();
    }

    @Test
    void getTotalDataSize_returnsZero() {
        assertThat(service.getTotalDataSize(1L)).isZero();
    }

    @Test
    void getCompanyUserIds_returnsEmptyList() {
        assertThat(service.getCompanyUserIds(1L)).isEmpty();
    }

    @Test
    void isUserInCompany_returnsFalse() {
        assertThat(service.isUserInCompany(1L, 1L)).isFalse();
    }

    @Test
    void searchCompanies_delegatesToFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Company> page = new PageImpl<>(List.of(savedCompany));
        when(companyRepository.findAll(pageable)).thenReturn(page);

        Page<Company> result = service.searchCompanies("acme", pageable);

        assertThat(result.getContent()).containsExactly(savedCompany);
        verify(companyRepository).findAll(pageable);
    }
}

