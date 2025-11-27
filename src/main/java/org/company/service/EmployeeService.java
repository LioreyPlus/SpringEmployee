package org.company.service;

import org.company.dto.EmployeeDTO;
import org.company.entity.Employee;
import org.company.mapper.EmployeeMapper;
import org.company.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;

    public List<EmployeeDTO> findAll() {
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.toDTOList(employees);
    }

    public Optional<EmployeeDTO> findById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toDTO);
    }

    public List<EmployeeDTO> findBySalaryRange(Long minSalary, Long maxSalary) {
        List<Employee> employees = employeeRepository.findBySalaryBetween(minSalary, maxSalary);
        return employeeMapper.toDTOList(employees);
    }

    public Optional<EmployeeDTO> getEmployeeWithProjects(Long id) {
        List<Employee> employees = employeeRepository.findEmployeeWithProjects(id);
        return employees.isEmpty() ? Optional.empty() :
                Optional.of(employeeMapper.toDTO(employees.get(0)));
    }

    public EmployeeDTO create(Employee employee) {
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toDTO(saved);
    }

    public Optional<EmployeeDTO> update(Long id, Employee employee) {
        return employeeRepository.findById(id)
                .map(existing -> {
                    employee.setId(id);
                    Employee updated = employeeRepository.save(employee);
                    return employeeMapper.toDTO(updated);
                });
    }

    public boolean delete(Long id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return true;
        }
        return false;
    }
}