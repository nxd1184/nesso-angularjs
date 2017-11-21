package vn.com.la.repository;

import vn.com.la.domain.JobTeam;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the JobTeam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobTeamRepository extends JpaRepository<JobTeam, Long> {

}
