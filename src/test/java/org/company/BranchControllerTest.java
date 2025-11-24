package org.company;

import org.company.controller.BranchController;
import org.company.dto.BranchDTO;
import org.company.dto.BranchStatsDTO;
import org.company.entity.Branch;
import org.company.mapper.BranchMapper;
import org.company.repository.BranchRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BranchController.class)
class BranchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BranchRepository branchRepository;

    @MockBean
    private BranchMapper branchMapper;

    @Test
    void findAllBranches_ShouldReturnAllBranches() throws Exception {
        Branch branch1 = new Branch(1L, "Main Branch", "Address 1", null, null);
        Branch branch2 = new Branch(2L, "Second Branch", "Address 2", null, null);
        List<Branch> branches = Arrays.asList(branch1, branch2);

        BranchDTO branchDTO1 = new BranchDTO(1L, "Main Branch", "Address 1");
        BranchDTO branchDTO2 = new BranchDTO(2L, "Second Branch", "Address 2");
        List<BranchDTO> branchDTOs = Arrays.asList(branchDTO1, branchDTO2);

        when(branchRepository.findAll()).thenReturn(branches);
        when(branchMapper.toDTOList(branches)).thenReturn(branchDTOs);

        mockMvc.perform(get("/api/branches"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Main Branch")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Second Branch")));
    }

    @Test
    void findBranchById_WhenBranchExists_ShouldReturnBranch() throws Exception {

        Branch branch = new Branch(1L, "Main Branch", "Address 1", null, null);
        BranchDTO branchDTO = new BranchDTO(1L, "Main Branch", "Address 1");

        when(branchRepository.findById(1L)).thenReturn(Optional.of(branch));
        when(branchMapper.toDTO(branch)).thenReturn(branchDTO);

        mockMvc.perform(get("/api/branches/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Main Branch")));
    }

    @Test
    void findBranchById_WhenBranchNotExists_ShouldReturnNotFound() throws Exception {

        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/branches/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBranchEmployeeStats_ShouldReturnStats() throws Exception {

        BranchStatsDTO stat1 = new BranchStatsDTO("Main Branch", 10L);
        BranchStatsDTO stat2 = new BranchStatsDTO("Second Branch", 5L);
        List<BranchStatsDTO> stats = Arrays.asList(stat1, stat2);

        when(branchRepository.findBranchEmployeeStats()).thenReturn(stats);

        mockMvc.perform(get("/api/branches/employee-stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].branchName", is("Main Branch")))
                .andExpect(jsonPath("$[0].employeeCount", is(10)))
                .andExpect(jsonPath("$[1].branchName", is("Second Branch")))
                .andExpect(jsonPath("$[1].employeeCount", is(5)));
    }

    @Test
    void getBranchEmployeeStats_WhenNoContent_ShouldReturnNoContent() throws Exception {

        when(branchRepository.findBranchEmployeeStats()).thenReturn(List.of());


        mockMvc.perform(get("/api/branches/employee-stats"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createBranch_ShouldReturnCreatedBranch() throws Exception {

        Branch branch = new Branch(null, "New Branch", "New Address", null, null);
        Branch savedBranch = new Branch(1L, "New Branch", "New Address", null, null);
        BranchDTO savedBranchDTO = new BranchDTO(1L, "New Branch", "New Address");

        when(branchRepository.save(any(Branch.class))).thenReturn(savedBranch);
        when(branchMapper.toDTO(savedBranch)).thenReturn(savedBranchDTO);

        String branchJson = """
            {
                "name": "New Branch",
                "address": "New Address"
            }
            """;

        mockMvc.perform(post("/api/branches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(branchJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Branch")));
    }

    @Test
    void updateBranch_WhenBranchExists_ShouldReturnUpdatedBranch() throws Exception {

        Branch existingBranch = new Branch(1L, "Old Name", "Old Address", null, null);
        Branch updatedBranch = new Branch(1L, "Updated Name", "Updated Address", null, null);
        BranchDTO updatedBranchDTO = new BranchDTO(1L, "Updated Name", "Updated Address");

        when(branchRepository.findById(1L)).thenReturn(Optional.of(existingBranch));
        when(branchRepository.save(any(Branch.class))).thenReturn(updatedBranch);
        when(branchMapper.toDTO(updatedBranch)).thenReturn(updatedBranchDTO);

        String updateJson = """
            {
                "name": "Updated Name",
                "address": "Updated Address"
            }
            """;

        mockMvc.perform(put("/api/branches/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Name")));
    }

    @Test
    void updateBranch_WhenBranchNotExists_ShouldReturnNotFound() throws Exception {

        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        String updateJson = """
            {
                "name": "Updated Name",
                "address": "Updated Address"
            }
            """;

        mockMvc.perform(put("/api/branches/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBranch_WhenBranchExists_ShouldReturnNoContent() throws Exception {

        Branch existingBranch = new Branch(1L, "Main Branch", "Address 1", null, null);
        when(branchRepository.findById(1L)).thenReturn(Optional.of(existingBranch));
        doNothing().when(branchRepository).deleteById(1L);

        mockMvc.perform(delete("/api/branches/1"))
                .andExpect(status().isNoContent());

        verify(branchRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBranch_WhenBranchNotExists_ShouldReturnNotFound() throws Exception {

        when(branchRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/branches/1"))
                .andExpect(status().isNotFound());

        verify(branchRepository, never()).deleteById(anyLong());
    }
}