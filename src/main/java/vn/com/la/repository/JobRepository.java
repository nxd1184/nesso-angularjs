package vn.com.la.repository;

import vn.com.la.domain.Job;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


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
}
