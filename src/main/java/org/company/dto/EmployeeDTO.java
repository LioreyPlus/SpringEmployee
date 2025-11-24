package org.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.company.Role;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String name;
    private Long salary;
    private String title;
    private Role role;
    private Long chiefId;
    private String chiefName;
    private Long branchId;
    private String branchName;
    private List<ProjectDTO> projects;

    public EmployeeDTO(Long id, String name, Long salary, String title, Role role) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.title = title;
        this.role = role;
    }
}