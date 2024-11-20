package com.airtribe.employeeManagement.Service.impl;

import com.airtribe.employeeManagement.Entity.Employee;
import com.airtribe.employeeManagement.Exception.ResourceNotFoundException;
import com.airtribe.employeeManagement.Repository.DepartmentRepository;
import com.airtribe.employeeManagement.Repository.EmployeeRepository;
import com.airtribe.employeeManagement.Repository.ProjectRepository;
import com.airtribe.employeeManagement.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Cacheable("employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    @Cacheable(value = "employee", key = "#id")
    public Employee getEmployeeById(Long id, Authentication auth) throws AccessDeniedException {
        Optional<Employee> byId = employeeRepository.findById(id);
        if(byId.isPresent()){
            if(auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLES_ADMIN"))){
             return byId.get();
            }
            throw new AccessDeniedException("Unauthorized to access this employee");
        }
        throw new ResourceNotFoundException("Employee not found with id: " + id);

    }

    @Override
    @CacheEvict(value = "employees", allEntries = true)
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    @CacheEvict(value = "employees", allEntries = true)
    @CachePut(value = "employee", key = "#id")
    public Employee updateEmployee(Long id, Employee employee) {
        Employee existingEmployee  = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        existingEmployee.setName(employee.getName());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setProjects(employee.getProjects());
        return employeeRepository.save(existingEmployee);
    }

    @Override
    @CacheEvict(value = {"employees", "employee"}, allEntries = true)
    public void deleteEmployee(Long id) {
       if(!employeeRepository.existsById(id)){
           throw new ResourceNotFoundException("Employee not found with id: " + id);
       }
        employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> searchEmployees(String name, Long departmentId, Long projectId) {
        if(departmentId!=null){
            return employeeRepository.findByDepartment_Id(departmentId);
        } else if (projectId != null) {
            return projectRepository.findByEmployees_Id(projectId).stream()
                    .flatMap(project->project.getEmployees().stream())
                    .collect(Collectors.toList());
        } else if (name!=null && !name.isEmpty()) {
            return employeeRepository.findAll().stream()
                    .filter(e->e.getName().contains(name))
                    .collect(Collectors.toList());
        }
        return employeeRepository.findAll();
    }
}
