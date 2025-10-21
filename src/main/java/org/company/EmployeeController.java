package org.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeRepository repository;

    @Value("${employee.name}")
    private String name;

    @Value("${employee.salary}")
    private int salary;

    @GetMapping("/employees")
    public List<Employee> findAll() {

        if (repository.count() == 0) {
            Employee employee = new Employee();
            employee.setName(name);
            employee.setSalary(salary);
            repository.save(employee);

        }
        return (List<Employee>) repository.findAll();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> findById(@PathVariable long id) {

        if (!repository.existsById(id)) {
            Employee employee = new Employee();
            employee.setId(id);
            employee.setName(name);
            employee.setSalary(salary);
            repository.save(employee);

        }
        return repository.findById(id)
                .map(employee -> new ResponseEntity<>(employee, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        Employee saved = repository.save(employee);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> update(@PathVariable long id, @RequestBody Employee employee) {
        if (!repository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        employee.setId(id);
        Employee updated = repository.save(employee);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        if (!repository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}