package org.company;

import org.company.controller.EmployeeController;
import org.company.dto.EmployeeDTO;
import org.company.entity.Employee;
import org.company.mapper.EmployeeMapper;
import org.company.repository.EmployeeRepository;
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

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private EmployeeMapper employeeMapper;

    @Test
    void findAllEmployees_ShouldReturnAllEmployees() throws Exception {

        Employee employee1 = new Employee(1L, "John Doe", 50000L, "Developer", Role.DEVELOPER, null, null, null);
        Employee employee2 = new Employee(2L, "Jane Smith", 60000L, "Manager", Role.MANAGER, null, null, null);
        List<Employee> employees = Arrays.asList(employee1, employee2);

        EmployeeDTO employeeDTO1 = new EmployeeDTO(1L, "John Doe", 50000L, "Developer", Role.DEVELOPER);
        EmployeeDTO employeeDTO2 = new EmployeeDTO(2L, "Jane Smith", 60000L, "Manager", Role.MANAGER);
        List<EmployeeDTO> employeeDTOs = Arrays.asList(employeeDTO1, employeeDTO2);

        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.toDTOList(employees)).thenReturn(employeeDTOs);


        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].name", is("Jane Smith")));
    }

    @Test
    void findEmployeesBySalaryRange_ShouldReturnFilteredEmployees() throws Exception {

        Employee employee = new Employee(1L, "John Doe", 75000L, "Developer", Role.DEVELOPER, null, null, null);
        List<Employee> employees = List.of(employee);
        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "John Doe", 75000L, "Developer", Role.DEVELOPER);
        List<EmployeeDTO> employeeDTOs = List.of(employeeDTO);

        when(employeeRepository.findBySalaryBetween(50000L, 100000L)).thenReturn(employees);
        when(employeeMapper.toDTOList(employees)).thenReturn(employeeDTOs);

        mockMvc.perform(get("/api/employees/by-salary")
                        .param("minSalary", "50000")
                        .param("maxSalary", "100000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].salary", is(75000)));
    }

    @Test
    void findEmployeesBySalaryRange_WhenNoEmployees_ShouldReturnNoContent() throws Exception {

        when(employeeRepository.findBySalaryBetween(anyLong(), anyLong())).thenReturn(List.of());

        mockMvc.perform(get("/api/employees/by-salary")
                        .param("minSalary", "100000")
                        .param("maxSalary", "200000"))
                .andExpect(status().isNoContent());
    }

    @Test
    void findEmployeeById_WhenEmployeeExists_ShouldReturnEmployee() throws Exception {

        Employee employee = new Employee(1L, "John Doe", 50000L, "Developer", Role.DEVELOPER, null, null, null);
        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "John Doe", 50000L, "Developer", Role.DEVELOPER);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    void getEmployeeWithProjects_ShouldReturnEmployeeWithProjects() throws Exception {

        Employee employee = new Employee(1L, "John Doe", 50000L, "Developer", Role.DEVELOPER, null, null, null);
        EmployeeDTO employeeDTO = new EmployeeDTO(1L, "John Doe", 50000L, "Developer", Role.DEVELOPER);

        when(employeeRepository.findEmployeeWithProjects(1L)).thenReturn(List.of(employee));
        when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

        mockMvc.perform(get("/api/employees/1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void createEmployee_ShouldReturnCreatedEmployee() throws Exception {

        Employee employee = new Employee(null, "New Employee", 50000L, "Developer", Role.DEVELOPER, null, null, null);
        Employee savedEmployee = new Employee(1L, "New Employee", 50000L, "Developer", Role.DEVELOPER, null, null, null);
        EmployeeDTO savedEmployeeDTO = new EmployeeDTO(1L, "New Employee", 50000L, "Developer", Role.DEVELOPER);

        when(employeeRepository.save(any(Employee.class))).thenReturn(savedEmployee);
        when(employeeMapper.toDTO(savedEmployee)).thenReturn(savedEmployeeDTO);

        String employeeJson = """
            {
                "name": "New Employee",
                "salary": 50000,
                "title": "Developer",
                "role": "DEVELOPER"
            }
            """;

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Employee")));
    }
}