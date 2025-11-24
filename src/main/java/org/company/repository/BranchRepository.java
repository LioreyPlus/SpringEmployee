package org.company.repository;

import org.company.dto.BranchStatsDTO;
import org.company.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "branches", path = "branches")
public interface BranchRepository extends JpaRepository<Branch, Long> {
    List<Branch> findByName(@Param("name") String name);
    @Query("""
            SELECT new org.company.dto.BranchStatsDTO(b.name, COUNT(e.id))
            FROM Branch b
            LEFT JOIN b.employees e
            GROUP BY b.id, b.name
            ORDER BY COUNT(e.id) DESC
            """)
    List<BranchStatsDTO> findBranchEmployeeStats();
}
