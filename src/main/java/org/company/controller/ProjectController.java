package org.company.controller;

import org.company.dto.ProjectDTO;
import org.company.dto.ProjectStatsDTO;
import org.company.entity.Project;
import org.company.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/projects")
    public List<ProjectDTO> findAll() {
        return projectService.findAll();
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectDTO> findById(@PathVariable long id) {
        return projectService.findById(id)
                .map(project -> new ResponseEntity<>(project, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/projects/employee-stats")
    public ResponseEntity<List<ProjectStatsDTO>> getProjectEmployeeStats() {
        try {
            List<ProjectStatsDTO> stats = projectService.getProjectEmployeeStats();

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
            return projectService.getProjectWithEmployees(id)
                    .map(project -> new ResponseEntity<>(project, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectDTO> create(@RequestBody Project project) {
        ProjectDTO savedDTO = projectService.create(project);
        return new ResponseEntity<>(savedDTO, HttpStatus.CREATED);
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<ProjectDTO> update(@PathVariable long id, @RequestBody Project project) {
        return projectService.update(id, project)
                .map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        return projectService.delete(id)
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}