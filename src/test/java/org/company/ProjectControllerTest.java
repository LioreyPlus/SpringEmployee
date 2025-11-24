package org.company;

import org.company.controller.ProjectController;
import org.company.dto.ProjectDTO;
import org.company.dto.ProjectStatsDTO;
import org.company.entity.Project;
import org.company.mapper.ProjectMapper;
import org.company.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;


import static org.hamcrest.Matchers.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private ProjectMapper projectMapper;

    @Test
    void getProjectEmployeeStats_ShouldReturnStats() throws Exception {

        ProjectStatsDTO stat1 = new ProjectStatsDTO("Project A", 5L);
        ProjectStatsDTO stat2 = new ProjectStatsDTO("Project B", 3L);
        List<ProjectStatsDTO> stats = Arrays.asList(stat1, stat2);

        when(projectRepository.findProjectEmployeeStats()).thenReturn(stats);

        mockMvc.perform(get("/api/projects/employee-stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].projectName", is("Project A")))
                .andExpect(jsonPath("$[0].employeeCount", is(5)))
                .andExpect(jsonPath("$[1].projectName", is("Project B")))
                .andExpect(jsonPath("$[1].employeeCount", is(3)));
    }

    @Test
    void getProjectWithEmployees_ShouldReturnProjectWithEmployees() throws Exception {

        Project project = new Project(1L, "Project A", null);
        ProjectDTO projectDTO = new ProjectDTO(1L, "Project A");

        when(projectRepository.findProjectWithEmployees(1L)).thenReturn(List.of(project));
        when(projectMapper.toDTO(project)).thenReturn(projectDTO);

        mockMvc.perform(get("/api/projects/1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Project A")));
    }

    @Test
    void getProjectWithEmployees_WhenProjectNotFound_ShouldReturnNotFound() throws Exception {

        when(projectRepository.findProjectWithEmployees(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/projects/1/employees"))
                .andExpect(status().isNotFound());
    }
}