package org.company.controller;

import org.company.dto.EmployeeDTO;
import org.company.entity.Employee;
import org.company.mapper.EmployeeMapper;
import org.company.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private EmployeeMapper employeeMapper;

    @GetMapping("/employees")
    public List<EmployeeDTO> findAll() {
        List<Employee> employees = repository.findAll();
        return employeeMapper.toDTOList(employees);
    }

    @GetMapping("/employees/by-salary")
    public ResponseEntity<List<EmployeeDTO>> findBySalaryRange(
            @RequestParam(defaultValue = "50000") Long minSalary,
            @RequestParam(defaultValue = "150000") Long maxSalary) {

        try {
            List<Employee> employees = repository.findBySalaryBetween(minSalary, maxSalary);

            if (employees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<EmployeeDTO> employeeDTOs = employeeMapper.toDTOList(employees);
            return new ResponseEntity<>(employeeDTOs, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable long id) {
        return repository.findById(id)
                .map(employee -> {
                    EmployeeDTO dto = employeeMapper.toDTO(employee);
                    return new ResponseEntity<>(dto, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/employees/{id}/projects")
    public ResponseEntity<EmployeeDTO> getEmployeeWithProjects(@PathVariable long id) {
        try {
            List<Employee> employees = repository.findEmployeeWithProjects(id);

            if (employees.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            EmployeeDTO employeeDTO = employeeMapper.toDTO(employees.get(0));
            return new ResponseEntity<>(employeeDTO, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/employees")
    public ResponseEntity<EmployeeDTO> create(@RequestBody Employee employee) {
        Employee saved = repository.save(employee);
        EmployeeDTO savedDTO = employeeMapper.toDTO(saved);
        return new ResponseEntity<>(savedDTO, HttpStatus.CREATED);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> update(@PathVariable long id, @RequestBody Employee employee) {
        return repository.findById(id)
                .map(existing -> {
                    employee.setId(id);
                    Employee updated = repository.save(employee);
                    EmployeeDTO updatedDTO = employeeMapper.toDTO(updated);
                    return new ResponseEntity<>(updatedDTO, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        return repository.findById(id)
                .map(existing -> {
                    repository.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}