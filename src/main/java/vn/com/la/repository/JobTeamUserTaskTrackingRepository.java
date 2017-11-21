package vn.com.la.repository;

import vn.com.la.domain.JobTeamUserTaskTracking;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the JobTeamUserTaskTracking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobTeamUserTaskTrackingRepository extends JpaRepository<JobTeamUserTaskTracking, Long> {

    @Query("select job_team_user_task_tracking from JobTeamUserTaskTracking job_team_user_task_tracking where job_team_user_task_tracking.user.login = ?#{principal.username}")
    List<JobTeamUserTaskTracking> findByUserIsCurrentUser();

}
