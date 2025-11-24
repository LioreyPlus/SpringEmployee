package org.company.controller;

import org.company.dto.BranchDTO;
import org.company.dto.BranchStatsDTO;
import org.company.entity.Branch;
import org.company.mapper.BranchMapper;
import org.company.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BranchController {

    @Autowired
    private BranchRepository repository;

    @Autowired
    private BranchMapper branchMapper;

    @GetMapping("/branches")
    public List<BranchDTO> findAll() {
        List<Branch> branches = repository.findAll();
        return branchMapper.toDTOList(branches);
    }

    @GetMapping("/branches/{id}")
    public ResponseEntity<BranchDTO> findById(@PathVariable long id) {
        return repository.findById(id)
                .map(branch -> {
                    BranchDTO dto = branchMapper.toDTO(branch);
                    return new ResponseEntity<>(dto, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/branches/employee-stats")
    public ResponseEntity<List<BranchStatsDTO>> getBranchEmployeeStats() {
        try {
            List<BranchStatsDTO> stats = repository.findBranchEmployeeStats();

            if (stats.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(stats, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/branches")
    public ResponseEntity<BranchDTO> create(@RequestBody Branch branch) {
        Branch saved = repository.save(branch);
        BranchDTO savedDTO = branchMapper.toDTO(saved);
        return new ResponseEntity<>(savedDTO, HttpStatus.CREATED);
    }

    @PutMapping("/branches/{id}")
    public ResponseEntity<BranchDTO> update(@PathVariable long id, @RequestBody Branch branch) {
        return repository.findById(id)
                .map(existing -> {
                    branch.setId(id);
                    Branch updated = repository.save(branch);
                    BranchDTO updatedDTO = branchMapper.toDTO(updated);
                    return new ResponseEntity<>(updatedDTO, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/branches/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        return repository.findById(id)
                .map(existing -> {
                    repository.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}