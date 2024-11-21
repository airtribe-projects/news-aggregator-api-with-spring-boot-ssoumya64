package com.airtribe.employeeManagement.Controller;

import com.airtribe.employeeManagement.Entity.Employee;
import com.airtribe.employeeManagement.Service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/employees")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public Employee getEmployeeById(@PathVariable Long id, Authentication auth) throws AccessDeniedException {
        return employeeService.getEmployeeById(id, auth);
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public Employee createEmployee(@RequestBody @Valid Employee employee) {
        return employeeService.saveEmployee(employee);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody @Valid Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<Employee> searchEmployees(@RequestParam(required = false) String name,
                                          @RequestParam(required = false) Long departmentId,
                                          @RequestParam(required = false) Long projectId) {
        return employeeService.searchEmployees(name, departmentId, projectId);
    }
}
