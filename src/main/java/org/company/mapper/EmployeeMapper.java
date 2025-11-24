package org.company.mapper;

import org.company.dto.EmployeeDTO;
import org.company.dto.ProjectDTO;
import org.company.entity.Employee;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {

    public EmployeeDTO toDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        Long chiefId = employee.getChief() != null ? employee.getChief().getId() : null;
        String chiefName = employee.getChief() != null ? employee.getChief().getName() : null;
        Long branchId = employee.getBranch() != null ? employee.getBranch().getId() : null;
        String branchName = employee.getBranch() != null ? employee.getBranch().getName() : null;

        EmployeeDTO dto = new EmployeeDTO(
                employee.getId(),
                employee.getName(),
                employee.getSalary(),
                employee.getTitle(),
                employee.getRole()
        );
        dto.setChiefId(chiefId);
        dto.setChiefName(chiefName);
        dto.setBranchId(branchId);
        dto.setBranchName(branchName);

        if (employee.getProjects() != null) {
            List<ProjectDTO> projectDTOs = employee.getProjects().stream()
                    .map(project -> new ProjectDTO(project.getId(), project.getName()))
                    .collect(Collectors.toList());
            dto.setProjects(projectDTOs);
        }

        return dto;
    }

    public Employee toEntity(EmployeeDTO dto) {
        if (dto == null) {
            return null;
        }

        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setName(dto.getName());
        employee.setSalary(dto.getSalary());
        employee.setTitle(dto.getTitle());
        employee.setRole(dto.getRole());

        return employee;
    }

    public List<EmployeeDTO> toDTOList(List<Employee> employees) {
        return employees.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}