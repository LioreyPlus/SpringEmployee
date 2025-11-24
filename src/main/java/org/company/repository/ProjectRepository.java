package org.company.repository;

import org.company.dto.ProjectStatsDTO;
import org.company.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "projects", path = "projects")
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByName(String name);

    @Query("""
        SELECT new org.company.dto.ProjectStatsDTO(p.name, COUNT(e.id)) 
        FROM Project p
        LEFT JOIN p.employees e
        GROUP BY p.id, p.name
        ORDER BY COUNT(e.id) DESC
        """)
    List<ProjectStatsDTO> findProjectEmployeeStats();

    @Query("""
            SELECT p
            FROM Project p
            JOIN FETCH p.employees
            WHERE p.id = :projectId
            """)
    List<Project> findProjectWithEmployees(@Param("projectId") Long projectId);


}