package org.company.mapper;

import org.company.dto.ProjectDTO;
import org.company.dto.EmployeeDTO;
import org.company.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    @Autowired
    private EmployeeMapper employeeMapper;

    public ProjectDTO toDTO(Project project) {
        if (project == null) {
            return null;
        }

        ProjectDTO dto = new ProjectDTO(
                project.getId(),
                project.getName()
        );

        if (project.getEmployees() != null) {
            List<EmployeeDTO> employeeDTOs = employeeMapper.toDTOList(project.getEmployees());
            dto.setEmployees(employeeDTOs);
        }

        return dto;
    }

    public Project toEntity(ProjectDTO dto) {
        if (dto == null) {
            return null;
        }

        Project project = new Project();
        project.setId(dto.getId());
        project.setName(dto.getName());

        return project;
    }

    public List<ProjectDTO> toDTOList(List<Project> projects) {
        return projects.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}