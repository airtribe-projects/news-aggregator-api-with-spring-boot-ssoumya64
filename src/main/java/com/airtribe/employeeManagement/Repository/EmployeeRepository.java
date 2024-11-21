package com.airtribe.employeeManagement.Repository;

import com.airtribe.employeeManagement.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
   List<Employee> findByDepartment_Id(Long departmentid);
   @Query(value = "SELECT e FROM Employee e WHERE e.projects IS EMPTY",nativeQuery = true)
   List<Employee> findEmployeesWithoutProjects();

}
