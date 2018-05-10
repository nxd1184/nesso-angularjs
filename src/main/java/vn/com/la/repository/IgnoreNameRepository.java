package vn.com.la.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.la.domain.IgnoreName;

/**
 * Spring Data JPA repository for the Job entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IgnoreNameRepository extends JpaRepository<IgnoreName, Long> {
}
