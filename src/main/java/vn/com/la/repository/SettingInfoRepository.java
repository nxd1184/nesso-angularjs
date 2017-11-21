package vn.com.la.repository;

import vn.com.la.domain.SettingInfo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SettingInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SettingInfoRepository extends JpaRepository<SettingInfo, Long> {

}
