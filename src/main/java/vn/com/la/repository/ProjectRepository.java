package vn.com.la.repository;

import vn.com.la.domain.Project;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import vn.com.la.service.specification.ProjectSpecifications;

import java.util.List;

/**
 * Spring Data JPA repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    @Query("select project from Project project where project.manager.login = ?#{principal.username}")
    List<Project> findByManagerIsCurrentUser();

}
