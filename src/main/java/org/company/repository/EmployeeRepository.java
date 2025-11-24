package org.company.repository;

import org.company.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "employees", path = "employees")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByName(String name);
    List<Employee> findByRole(String role);

    List<Employee> findBySalaryBetween(@Param("minSalary") Long minSalary, @Param("maxSalary") Long maxSalary);

    @Query("""
            SELECT e
            FROM Employee e
            WHERE e.branch.id = :branchId
           """)
    List<Employee> findByBranchId(@Param("branchId") Long branchId);

    @Query("""
    SELECT COUNT(e)
    FROM Employee e
    WHERE e.branch.id = :branchId
   """)
    Long countByBranchId(@Param("branchId") Long branchId);

    @Query("""
            SELECT e
            FROM Employee e
            JOIN FETCH e.projects
            WHERE e.id = :employeeId
            """)
    List<Employee> findEmployeeWithProjects(@Param("employeeId") Long employeeId);

}