package vn.com.la.repository;

import vn.com.la.domain.Team;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Team entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("select team from Team team where team.leader.login = ?#{principal.username}")
    List<Team> findByLeaderIsCurrentUser();

}
