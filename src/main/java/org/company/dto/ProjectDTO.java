package org.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String name;
    private List<EmployeeDTO> employees;

    public ProjectDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}