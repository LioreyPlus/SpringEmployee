package org.company.controller;

import org.company.dto.ProjectDTO;
import org.company.dto.ProjectStatsDTO;
import org.company.entity.Project;
import org.company.mapper.ProjectMapper;
import org.company.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    private ProjectRepository repository;

    @Autowired
    private ProjectMapper projectMapper;

    @GetMapping("/projects")
    public List<ProjectDTO> findAll() {
        List<Project> projects = repository.findAll();
        return projectMapper.toDTOList(projects);
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectDTO> findById(@PathVariable long id) {
        return repository.findById(id)
                .map(project -> {
                    ProjectDTO dto = projectMapper.toDTO(project);
                    return new ResponseEntity<>(dto, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/projects/employee-stats")
    public ResponseEntity<List<ProjectStatsDTO>> getProjectEmployeeStats() {
        try {
            List<ProjectStatsDTO> stats = repository.findProjectEmployeeStats();

            if (stats.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(stats, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/projects/{id}/employees")
    public ResponseEntity<ProjectDTO> getProjectWithEmployees(@PathVariable long id) {
        try {
            List<Project> projects = repository.findProjectWithEmployees(id);

            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            ProjectDTO projectDTO = projectMapper.toDTO(projects.get(0));
            return new ResponseEntity<>(projectDTO, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/projects")
    public ResponseEntity<ProjectDTO> create(@RequestBody Project project) {
        Project saved = repository.save(project);
        ProjectDTO savedDTO = projectMapper.toDTO(saved);
        return new ResponseEntity<>(savedDTO, HttpStatus.CREATED);
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<ProjectDTO> update(@PathVariable long id, @RequestBody Project project) {
        return repository.findById(id)
                .map(existing -> {
                    project.setId(id);
                    Project updated = repository.save(project);
                    ProjectDTO updatedDTO = projectMapper.toDTO(updated);
                    return new ResponseEntity<>(updatedDTO, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        return repository.findById(id)
                .map(existing -> {
                    repository.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}