package vn.com.la.repository;

import vn.com.la.domain.Job;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Spring Data JPA repository for the Job entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Job findByNameAndProjectCode(String name, String projectCode);

    @Modifying
    @Query("UPDATE Job SET started = true WHERE id = ?1")
    int updateJobToStarted(final Long id);

    @Modifying
    @Query("UPDATE Job SET deadline = ?1, customerRequirements = ?2 where id = ?3")
    int updateDeadLineAndCustomerRequirements(ZonedDateTime deadLine, String customerRequirement, Long jobId);

    @Query("SELECT SUM(totalFiles) FROM Job WHERE syncDate BETWEEN ?1 AND ?2")
    Long sumReceiveByDateRange(ZonedDateTime fromDate, ZonedDateTime toDate);

    List<Job> findByDeadlineBetweenOrderByDeadlineAsc(ZonedDateTime fromDate, ZonedDateTime toDate);
}
