package vn.com.la.repository;

import vn.com.la.domain.JobTeamUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the JobTeamUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobTeamUserRepository extends JpaRepository<JobTeamUser, Long> {

    @Query("select job_team_user from JobTeamUser job_team_user where job_team_user.user.login = ?#{principal.username}")
    List<JobTeamUser> findByUserIsCurrentUser();

    @Modifying
    @Query("UPDATE JobTeamUser SET totalFiles = ?2 WHERE id = ?1")
    int updateTotalFilesByJobTeamUserId(Long jobTeamUserId,Long newTotalFiles);

}
