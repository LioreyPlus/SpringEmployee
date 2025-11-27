package org.company.service;

import org.company.dto.ProjectDTO;
import org.company.dto.ProjectStatsDTO;
import org.company.entity.Project;
import org.company.mapper.ProjectMapper;
import org.company.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMapper projectMapper;

    public List<ProjectDTO> findAll() {
        List<Project> projects = projectRepository.findAll();
        return projectMapper.toDTOList(projects);
    }

    public Optional<ProjectDTO> findById(Long id) {
        return projectRepository.findById(id)
                .map(projectMapper::toDTO);
    }

    public List<ProjectStatsDTO> getProjectEmployeeStats() {
        return projectRepository.findProjectEmployeeStats();
    }

    public Optional<ProjectDTO> getProjectWithEmployees(Long id) {
        List<Project> projects = projectRepository.findProjectWithEmployees(id);
        return projects.isEmpty() ? Optional.empty() :
                Optional.of(projectMapper.toDTO(projects.get(0)));
    }

    public ProjectDTO create(Project project) {
        Project saved = projectRepository.save(project);
        return projectMapper.toDTO(saved);
    }

    public Optional<ProjectDTO> update(Long id, Project project) {
        return projectRepository.findById(id)
                .map(existing -> {
                    project.setId(id);
                    Project updated = projectRepository.save(project);
                    return projectMapper.toDTO(updated);
                });
    }

    public boolean delete(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }
}