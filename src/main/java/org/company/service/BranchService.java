package org.company.service;

import jakarta.transaction.Transactional;
import org.company.dto.BranchDTO;
import org.company.dto.BranchStatsDTO;
import org.company.entity.Branch;
import org.company.mapper.BranchMapper;
import org.company.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchMapper branchMapper;

    public List<BranchDTO> findAll(){
        List<Branch> branches = branchRepository.findAll();
        return branchMapper.toDTOList(branches);

    }

    public Optional<BranchDTO> findById(Long id) {
        return branchRepository.findById(id)
                .map(branchMapper::toDTO);
    }

    public List<BranchStatsDTO> getBranchEmployeeStats() {
        return branchRepository.findBranchEmployeeStats();
    }

    public BranchDTO create(Branch branch) {
        Branch saved = branchRepository.save(branch);
        return branchMapper.toDTO(saved);
    }

    public Optional<BranchDTO> update(Long id, Branch branch) {
        return branchRepository.findById(id)
                .map(existing ->{
                    branch.setId(id);
                    Branch updated = branchRepository.save(branch);
                    return branchMapper.toDTO(updated);
                });
    }

    public boolean delete(Long id) {
        if ((branchRepository.existsById(id))) {
            branchRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
