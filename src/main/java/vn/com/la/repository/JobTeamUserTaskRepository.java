package vn.com.la.repository;

import vn.com.la.domain.JobTeamUserTask;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import vn.com.la.domain.enumeration.FileStatusEnum;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the JobTeamUserTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobTeamUserTaskRepository extends JpaRepository<JobTeamUserTask, Long>, JpaSpecificationExecutor<JobTeamUserTask> {

//    @Query("select job_team_user_task from JobTeamUserTask job_team_user_task where job_team_user_task.assignee.login = ?#{principal.username}")
//    List<JobTeamUserTask> findByAssigneeIsCurrentUser();
//
//    @Query("select job_team_user_task from JobTeamUserTask job_team_user_task where job_team_user_task.qc.login = ?#{principal.username}")
//    List<JobTeamUserTask> findByQcIsCurrentUser();

    JobTeamUserTask findByFileName(String fileName);

    List<JobTeamUserTask> findByFileNameInAndStatus(List<String> fileNames, FileStatusEnum status);

    Long countByStatusInAndLastModifiedDateIsBetween(List<FileStatusEnum> statusList, Instant fromDate, Instant toDate);

    @Query("select COALESCE(count(numberOfRework),0) from JobTeamUserTask where lastReworkTime between ?1 and ?2")
    Long sumNumberOfReworkByLastReworkTimeIsBetween(ZonedDateTime fromDate, ZonedDateTime toDate);

    List<JobTeamUserTask> findByJobTeamUserId(Long id);

    List<JobTeamUserTask> findByJobTeamUserIdAndJobTeamUserJobTeamJobId(Long id, Long jobId);
    List<JobTeamUserTask> findByJobTeamUserIdAndStatusInAndJobTeamUserJobTeamJobId(Long id, List<FileStatusEnum> statusList, Long jobId);

    Long countByJobTeamUserIdAndStatusInAndJobTeamUserJobTeamJobId(Long id, List<FileStatusEnum> statusList, Long jobId);

    Long countByJobTeamUserJobTeamJobIdAndStatusIn(Long jobId, List<FileStatusEnum> statusEnumList);

    @Modifying
    @Query("update JobTeamUserTask set jobTeamUser.id = ?1, filePath = ?2 where id = ?3")
    int updateAdjustment(Long jobTeamUserId, String newFilePath, Long id);

    @Modifying
    @Query("delete from JobTeamUserTask where id = ?1")
    int deleteById(Long id);

}
