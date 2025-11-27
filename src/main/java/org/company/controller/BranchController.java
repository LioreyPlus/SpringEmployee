package org.company.controller;

import org.company.dto.BranchDTO;
import org.company.dto.BranchStatsDTO;
import org.company.entity.Branch;
import org.company.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @GetMapping("/branches")
    public List<BranchDTO> findAll() {
        return branchService.findAll();
    }

    @GetMapping("/branches/{id}")
    public ResponseEntity<BranchDTO> findById(@PathVariable long id) {
        return branchService.findById(id)
                .map(branch -> new ResponseEntity<>(branch, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/branches/employee-stats")
    public ResponseEntity<List<BranchStatsDTO>> getBranchEmployeeStats() {
        try {
            List<BranchStatsDTO> stats = branchService.getBranchEmployeeStats();

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
        BranchDTO savedDTO = branchService.create(branch);
        return new ResponseEntity<>(savedDTO, HttpStatus.CREATED);
    }

    @PutMapping("/branches/{id}")
    public ResponseEntity<BranchDTO> update(@PathVariable long id, @RequestBody Branch branch) {
        return branchService.update(id, branch)
                .map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/branches/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        return branchService.delete(id)
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}