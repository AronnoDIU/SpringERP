package com.springerp.controller;
import com.springerp.entity.Employee;
import com.springerp.service.HRService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {
    private final HRService hrService;
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        return new ResponseEntity<>(hrService.createEmployee(employee), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) {
        return ResponseEntity.ok(hrService.updateEmployee(id, employee));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        hrService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return hrService.getEmployee(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<Page<Employee>> getAllEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(hrService.getAllEmployees(PageRequest.of(page, size, Sort.by(sortBy))));
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<Employee>> getByStatus(
            @PathVariable Employee.EmploymentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(hrService.getEmployeesByStatus(status, PageRequest.of(page, size)));
    }
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<Page<Employee>> getByDepartment(
            @PathVariable Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(hrService.getEmployeesByDepartment(departmentId, PageRequest.of(page, size)));
    }
    @GetMapping("/manager/{managerId}/reports")
    public ResponseEntity<List<Employee>> getReportingEmployees(@PathVariable Long managerId) {
        return ResponseEntity.ok(hrService.getReportingEmployees(managerId));
    }
}
