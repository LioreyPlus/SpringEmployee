package org.company.mapper;

import org.company.dto.BranchDTO;
import org.company.dto.EmployeeDTO;
import org.company.entity.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BranchMapper {

    @Autowired
    private EmployeeMapper employeeMapper;

    public BranchDTO toDTO(Branch branch) {
        if (branch == null) {
            return null;
        }

        Long managerId = branch.getManager() != null ? branch.getManager().getId() : null;
        String managerName = branch.getManager() != null ? branch.getManager().getName() : null;

        BranchDTO dto = new BranchDTO(
                branch.getId(),
                branch.getName(),
                branch.getAddress()
        );
        dto.setManagerId(managerId);
        dto.setManagerName(managerName);

        if (branch.getEmployees() != null) {
            List<EmployeeDTO> employeeDTOs = employeeMapper.toDTOList(branch.getEmployees());
            dto.setEmployees(employeeDTOs);
        }

        return dto;
    }

    public Branch toEntity(BranchDTO dto) {
        if (dto == null) {
            return null;
        }

        Branch branch = new Branch();
        branch.setId(dto.getId());
        branch.setName(dto.getName());
        branch.setAddress(dto.getAddress());

        return branch;
    }

    public List<BranchDTO> toDTOList(List<Branch> branches) {
        return branches.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}