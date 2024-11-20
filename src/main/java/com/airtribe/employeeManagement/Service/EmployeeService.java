package com.airtribe.employeeManagement.Service;

import com.airtribe.employeeManagement.Entity.Employee;
import org.springframework.security.core.Authentication;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Employee getEmployeeById(Long id, Authentication auth) throws AccessDeniedException;
    Employee saveEmployee(Employee employee);
    Employee updateEmployee(Long id, Employee employee);
    void deleteEmployee(Long id);
    List<Employee> searchEmployees(String name, Long departmentId, Long projectId);
}
