package org.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmployeeCreator implements CommandLineRunner {

    @Autowired
    private EmployeeRepository repository;

    @Value("${employee.id}")
    private long id;

    @Value("${employee.name}")
    private String name;

    @Value("${employee.salary}")
    private int salary;

    @Override
    public void run(String... args) throws Exception {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName(name);
        employee.setSalary(salary);
        repository.save(employee);

    }
}