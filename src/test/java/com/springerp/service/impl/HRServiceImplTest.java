package com.springerp.service.impl;

import com.springerp.entity.Attendance;
import com.springerp.entity.Department;
import com.springerp.entity.Employee;
import com.springerp.entity.Leave;
import com.springerp.exception.ResourceNotFoundException;
import com.springerp.repository.AttendanceRepository;
import com.springerp.repository.DepartmentRepository;
import com.springerp.repository.EmployeeRepository;
import com.springerp.repository.LeaveRepository;
import com.springerp.repository.SalaryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HRServiceImplTest {

    @Mock private DepartmentRepository departmentRepository;
    @Mock private EmployeeRepository employeeRepository;
    @Mock private AttendanceRepository attendanceRepository;
    @Mock private LeaveRepository leaveRepository;
    @Mock private SalaryRepository salaryRepository;

    @InjectMocks
    private HRServiceImpl service;

    private Department department;
    private Employee employee;
    private Attendance attendance;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setDepartmentCode("ENG");
        department.setDepartmentName("Engineering");
        department.setBudget(new BigDecimal("100000"));

        employee = new Employee();
        employee.setId(1L);
        employee.setEmployeeId("EMP-001");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@company.com");
        employee.setDesignation("Senior Engineer");
        employee.setBaseSalary(new BigDecimal("80000"));

        attendance = new Attendance();
        attendance.setId(1L);
        attendance.setEmployee(employee);
        attendance.setAttendanceDate(LocalDate.now());
        attendance.setAttendanceStatus(Attendance.AttendanceStatus.PRESENT);
        attendance.setCheckInTime(LocalTime.of(9, 0));
        attendance.setCheckOutTime(LocalTime.of(17, 0));
        attendance.setWorkingHours(8.0);
        attendance.setIsApproved(false);
    }

    // ─── Department ───────────────────────────────────────────────────────────

    @Test
    void createDepartment_savesAndReturnsDepartment() {
        when(departmentRepository.save(any(Department.class))).thenReturn(department);

        Department result = service.createDepartment(department);

        assertThat(result).isEqualTo(department);
        verify(departmentRepository).save(department);
    }

    @Test
    void updateDepartment_updatesNameDescriptionAndBudget() {
        Department incoming = new Department();
        incoming.setDepartmentName("Software Engineering");
        incoming.setDescription("All software teams");
        incoming.setBudget(new BigDecimal("150000"));

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.save(any(Department.class))).thenAnswer(inv -> inv.getArgument(0));

        Department result = service.updateDepartment(1L, incoming);

        assertThat(result.getDepartmentName()).isEqualTo("Software Engineering");
        assertThat(result.getDescription()).isEqualTo("All software teams");
        assertThat(result.getBudget()).isEqualByComparingTo("150000");
    }

    @Test
    void updateDepartment_withNonExistentId_throwsResourceNotFoundException() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateDepartment(99L, department))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getDepartment_returnsExistingDepartment() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        Optional<Department> result = service.getDepartment(1L);

        assertThat(result).contains(department);
    }

    @Test
    void getDepartment_withNonExistentId_returnsEmpty() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Department> result = service.getDepartment(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void getAllDepartments_returnsPaginatedResults() {
        Page<Department> page = new PageImpl<>(List.of(department));
        when(departmentRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Department> result = service.getAllDepartments(PageRequest.of(0, 10));

        assertThat(result.getContent()).containsExactly(department);
    }

    // ─── Employee ─────────────────────────────────────────────────────────────

    @Test
    void createEmployee_setsIsActiveTrueAndStatusActive() {
        employee.setIsActive(false);
        employee.setEmploymentStatus(null);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        service.createEmployee(employee);

        ArgumentCaptor<Employee> captor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(captor.capture());
        assertThat(captor.getValue().getIsActive()).isTrue();
        assertThat(captor.getValue().getEmploymentStatus()).isEqualTo(Employee.EmploymentStatus.ACTIVE);
    }

    @Test
    void createEmployee_savesAndReturnsEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee result = service.createEmployee(employee);

        assertThat(result).isEqualTo(employee);
    }

    @Test
    void updateEmployee_updatesEditableFields() {
        Employee incoming = new Employee();
        incoming.setFirstName("Jane");
        incoming.setLastName("Smith");
        incoming.setEmail("jane.smith@company.com");
        incoming.setDesignation("Tech Lead");
        incoming.setBaseSalary(new BigDecimal("95000"));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        Employee result = service.updateEmployee(1L, incoming);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Smith");
        assertThat(result.getEmail()).isEqualTo("jane.smith@company.com");
        assertThat(result.getDesignation()).isEqualTo("Tech Lead");
        assertThat(result.getBaseSalary()).isEqualByComparingTo("95000");
    }

    @Test
    void updateEmployee_withNonExistentId_throwsResourceNotFoundException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateEmployee(99L, employee))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getEmployee_returnsExistingEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Optional<Employee> result = service.getEmployee(1L);

        assertThat(result).contains(employee);
    }

    @Test
    void getEmployee_withNonExistentId_returnsEmpty() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Employee> result = service.getEmployee(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void getEmployeeByEmployeeId_delegatesToRepository() {
        when(employeeRepository.findByEmployeeId("EMP-001")).thenReturn(Optional.of(employee));

        Optional<Employee> result = service.getEmployeeByEmployeeId("EMP-001");

        assertThat(result).contains(employee);
    }

    @Test
    void getEmployeeByEmail_delegatesToRepository() {
        when(employeeRepository.findByEmail("john.doe@company.com")).thenReturn(Optional.of(employee));

        Optional<Employee> result = service.getEmployeeByEmail("john.doe@company.com");

        assertThat(result).contains(employee);
    }

    @Test
    void getReportingEmployees_returnsEmployeesUnderManager() {
        when(employeeRepository.findByReportingManagerId(1L)).thenReturn(List.of(employee));

        List<Employee> result = service.getReportingEmployees(1L);

        assertThat(result).containsExactly(employee);
    }

    @Test
    void getReportingEmployees_returnsEmptyListWhenNoReports() {
        when(employeeRepository.findByReportingManagerId(99L)).thenReturn(List.of());

        List<Employee> result = service.getReportingEmployees(99L);

        assertThat(result).isEmpty();
    }

    // ─── Attendance ───────────────────────────────────────────────────────────

    @Test
    void recordAttendance_savesAndReturnsAttendance() {
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);

        Attendance result = service.recordAttendance(attendance);

        assertThat(result).isEqualTo(attendance);
        verify(attendanceRepository).save(attendance);
    }

    @Test
    void updateAttendance_updatesStatusAndTimes() {
        Attendance incoming = new Attendance();
        incoming.setAttendanceStatus(Attendance.AttendanceStatus.HALF_DAY);
        incoming.setCheckInTime(LocalTime.of(9, 0));
        incoming.setCheckOutTime(LocalTime.of(13, 0));
        incoming.setWorkingHours(4.0);

        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(attendance));
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(inv -> inv.getArgument(0));

        Attendance result = service.updateAttendance(1L, incoming);

        assertThat(result.getAttendanceStatus()).isEqualTo(Attendance.AttendanceStatus.HALF_DAY);
        assertThat(result.getWorkingHours()).isEqualTo(4.0);
    }

    @Test
    void updateAttendance_withNonExistentId_throwsResourceNotFoundException() {
        when(attendanceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateAttendance(99L, attendance))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void approveAttendance_setsIsApprovedAndApprover() {
        when(attendanceRepository.findById(1L)).thenReturn(Optional.of(attendance));
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(inv -> inv.getArgument(0));

        Attendance result = service.approveAttendance(1L, 42L);

        assertThat(result.getIsApproved()).isTrue();
        assertThat(result.getApprovedBy()).isEqualTo(42L);
    }

    @Test
    void approveAttendance_withNonExistentId_throwsResourceNotFoundException() {
        when(attendanceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.approveAttendance(99L, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getPresenceCount_countsOnlyPresentAttendance() {
        Attendance absent = new Attendance();
        absent.setAttendanceStatus(Attendance.AttendanceStatus.ABSENT);

        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();

        when(attendanceRepository.findByEmployeeIdAndAttendanceDateBetween(1L, start, end))
                .thenReturn(List.of(attendance, absent));

        long count = service.getPresenceCount(1L, start, end);

        assertThat(count).isEqualTo(1);
    }

    @Test
    void getAbsenceCount_countsOnlyAbsentAttendance() {
        Attendance absent = new Attendance();
        absent.setAttendanceStatus(Attendance.AttendanceStatus.ABSENT);

        Attendance absent2 = new Attendance();
        absent2.setAttendanceStatus(Attendance.AttendanceStatus.ABSENT);

        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();

        when(attendanceRepository.findByEmployeeIdAndAttendanceDateBetween(1L, start, end))
                .thenReturn(List.of(attendance, absent, absent2));

        long count = service.getAbsenceCount(1L, start, end);

        assertThat(count).isEqualTo(2);
    }

    @Test
    void getPresenceCount_returnsZeroWhenNoAttendanceRecords() {
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();

        when(attendanceRepository.findByEmployeeIdAndAttendanceDateBetween(99L, start, end))
                .thenReturn(List.of());

        long count = service.getPresenceCount(99L, start, end);

        assertThat(count).isZero();
    }
}

