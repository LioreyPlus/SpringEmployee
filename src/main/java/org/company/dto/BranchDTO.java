package org.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchDTO {
    private Long id;
    private String name;
    private String address;
    private Long managerId;
    private String managerName;
    private List<EmployeeDTO> employees;

    public BranchDTO(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }
}