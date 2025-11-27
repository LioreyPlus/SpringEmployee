package org.company.controller;

import org.company.dto.EmployeeDTO;
import org.company.entity.Employee;
import org.company.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public List<EmployeeDTO> findAll() {
        return employeeService.findAll();
    }

    @GetMapping("/employees/by-salary")
    public ResponseEntity<List<EmployeeDTO>> findBySalaryRange(
            @RequestParam(defaultValue = "50000") Long minSalary,
            @RequestParam(defaultValue = "150000") Long maxSalary) {

        try {
            List<EmployeeDTO> employeeDTOs = employeeService.findBySalaryRange(minSalary, maxSalary);

            if (employeeDTOs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(employeeDTOs, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable long id) {
        return employeeService.findById(id)
                .map(employee -> new ResponseEntity<>(employee, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/employees/{id}/projects")
    public ResponseEntity<EmployeeDTO> getEmployeeWithProjects(@PathVariable long id) {
        try {
            return employeeService.getEmployeeWithProjects(id)
                    .map(employee -> new ResponseEntity<>(employee, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/employees")
    public ResponseEntity<EmployeeDTO> create(@RequestBody Employee employee) {
        EmployeeDTO savedDTO = employeeService.create(employee);
        return new ResponseEntity<>(savedDTO, HttpStatus.CREATED);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> update(@PathVariable long id, @RequestBody Employee employee) {
        return employeeService.update(id, employee)
                .map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        return employeeService.delete(id)
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}